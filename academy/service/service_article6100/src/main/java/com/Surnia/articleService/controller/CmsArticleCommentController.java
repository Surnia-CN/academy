package com.Surnia.articleService.controller;


import cn.hutool.core.bean.BeanUtil;
import com.Surnia.articleService.entity.CmsArticle;
import com.Surnia.articleService.entity.CmsArticleComment;
import com.Surnia.articleService.entity.dto.CommentDTO;
import com.Surnia.articleService.entity.dto.QueryCommentDTO;
import com.Surnia.articleService.entity.edu.EduCourse;
import com.Surnia.articleService.entity.edu.EduVideo;
import com.Surnia.articleService.entity.vo.ArticleCommentVO;
import com.Surnia.articleService.feign.EduFeignService;
import com.Surnia.articleService.feign.UserFeignService;
import com.Surnia.articleService.service.CmsArticleCommentService;
import com.Surnia.articleService.service.CmsArticleService;
import com.Surnia.commons.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-09-14
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "文章系统评论管理")
@RestController
@RequestMapping("/articleService/comment")
public class CmsArticleCommentController {
    @Resource
    private CmsArticleCommentService cmsArticleCommentService;
    @Resource
    private CmsArticleService cmsArticleService;
    @Resource
    private UserFeignService userFeignService;
    @Resource
    private EduFeignService eduFeignService;


    @ApiOperation(value = "增加文章评论")
    @PostMapping("addComment")
    public Result addComment(@RequestBody CommentDTO commentDTO) {
        System.out.println(commentDTO);

        String commentNo = commentDTO.getCommentNo();
        // 文章的评论
        if ("1".equals(commentNo)){
            // 获取文章对象
            CmsArticle article = cmsArticleService.getById(commentDTO.getMainBodyId());
            if (article == null){
                return Result.error().message("找不到该文章");
            }
            // 增加评论对象（评论序号生成过程未加事务，存在序号重复的可能性 todo）
            boolean result = cmsArticleCommentService.addComment(commentDTO);
            if (result) {
                // 文章对象评论数+1
                article.setCommentCount(article.getCommentCount() + 1);
                // 更新文章对象数据
                QueryWrapper<CmsArticle> articleQueryWrapper = new QueryWrapper<>();
                articleQueryWrapper.eq("id", article.getId());
                cmsArticleService.update(article, articleQueryWrapper);
                return Result.ok();
            } else {
                return Result.error().message("发布评论失败");
            }
        }
        // 课程的评论
        if ("2".equals(commentNo)){
            EduCourse eduCourse = eduFeignService.getCourseByCourseId(commentDTO.getMainBodyId());
            if (eduCourse == null){
                return Result.error().message("找不到该课程");
            }
            // 增加评论对象（评论序号生成过程未加事务，存在序号重复的可能性 todo）
            boolean result = cmsArticleCommentService.addComment(commentDTO);
            if (result) {
                // 文章对象评论数+1
                eduCourse.setCommentCount(eduCourse.getCommentCount() + 1);
                // 更新文章对象数据
                result = eduFeignService.updateCourseByCourse(eduCourse);
                if(result){
                    return Result.ok();
                } else {
                    return Result.error().message("更新课程评论数失败");
                }
            } else {
                return Result.error().message("发布评论失败");
            }
        }
        // 视频的评论
        if ("3".equals(commentNo)){
            EduVideo eduVideo = eduFeignService.getVideoByVideoId(commentDTO.getMainBodyId());
            if (eduVideo == null){
                return Result.error().message("找不到该视频");
            }
            // 增加评论对象（评论序号生成过程未加事务，存在序号重复的可能性 todo）
            boolean result = cmsArticleCommentService.addComment(commentDTO);
            if (result) {
                // 文章对象评论数+1
                eduVideo.setCommentCount(eduVideo.getCommentCount() + 1);
                // 更新文章对象数据
                return eduFeignService.updateVideo(eduVideo);
            } else {
                return Result.error().message("发布评论失败");
            }
        }

        return Result.error().message("评论种类数据异常");

    }

    // 分页功能 todo
    @ApiOperation(value = "按主体信息查询所有评论")
    @PostMapping("getCommentByCommentQueryDTO")
    public  Result getCommentByCommentQueryDTO(@RequestBody QueryCommentDTO queryCommentDTO){
        String commentNo = queryCommentDTO.getCommentNo();
        String mainBodyId = queryCommentDTO.getMainBodyId();

        QueryWrapper<CmsArticleComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_no", commentNo);
        queryWrapper.eq("main_body_id", mainBodyId);
        queryWrapper.orderByAsc("gmt_create");
        List<CmsArticleComment> comments = cmsArticleCommentService.list(queryWrapper);
        // 创建一个返回总列
        List<ArticleCommentVO> articleCommentVOList = new ArrayList<>();

        for (CmsArticleComment comment : comments) {
            // 转换为VO数据格式
            ArticleCommentVO commentVO = BeanUtil.toBean(comment, ArticleCommentVO.class);
            // 查询用户信息
            HashMap<String, Object> userMap = userFeignService.getUserById(commentVO.getUserId());
            commentVO.setUserNickname((String) userMap.get("userNickname"));
            commentVO.setUserAvatar((String) userMap.get("userAvatar"));
            commentVO.setUserIsDisabled((Boolean) userMap.get("userIsDisabled"));



            // 创建子评论列表
            commentVO.setChildrenCommentList(new ArrayList<>());

            // 如果不是子评论，则父评论入总列
            if ("0".equals(comment.getParentId())){
                articleCommentVOList.add(commentVO);
            } else { // 如果是子评论，则调用stream流，对现有总列进行筛选，将符合条件的父评论进行处理
                Stream<ArticleCommentVO> voStream = articleCommentVOList.stream().filter(new Predicate<ArticleCommentVO>() {
                    @Override
                    public boolean test(ArticleCommentVO articleCommentVO) {
                        // 如果本次循环的评论的父评论id和之前某次评论的id相同，则入其子评论列
                        // 此步重点是筛选后的操作，所有元素均保留，而不是单纯的filter()用来筛选出符合条件元素
                        if(comment.getParentId().equals(articleCommentVO.getId())){
                            articleCommentVO.getChildrenCommentList().add(commentVO);
                        }
                        // 查询子评论回复对象的昵称
                        if (!"0".equals(commentVO.getAnswerId())){
                            CmsArticleComment answerComment = cmsArticleCommentService.getById(commentVO.getAnswerId());
                            HashMap<String, Object> answerMap = userFeignService.getUserById(answerComment.getUserId());
                            System.out.println("==========:"+answerMap);
                            commentVO.setAnswerName((String) answerMap.get("userNickname"));
                        }

                        // 所有元素返回true表示只处理总列中的父评论的子评论列，对总列所有元素均保留
                        return true;
                    }
                });
                // 将stream流转回为list
                articleCommentVOList = voStream.collect(Collectors.toList());
            }
        }
        return Result.ok().data("articleComments",articleCommentVOList);
    }


    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据文章id点赞")
    @PostMapping("addCommentLike/{commentId}")
    public boolean addCommentLike(@PathVariable("commentId") String commentId){
        CmsArticleComment articleComment = cmsArticleCommentService.getById(commentId);
        if(articleComment == null){
            return false;
        }
        articleComment.setLikeCount(articleComment.getLikeCount() + 1);
        return cmsArticleCommentService.updateById(articleComment);
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据文章id取消点赞")
    @DeleteMapping("deleteCommentLike/{commentId}")
    public boolean deleteCommentLike(@PathVariable("commentId") String commentId){
        CmsArticleComment articleComment = cmsArticleCommentService.getById(commentId);
        if(articleComment == null){
            return false;
        }
        articleComment.setLikeCount(articleComment.getLikeCount() - 1);
        return cmsArticleCommentService.updateById(articleComment);
    }
}

