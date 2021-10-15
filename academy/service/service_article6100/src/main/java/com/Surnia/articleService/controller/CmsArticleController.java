package com.Surnia.articleService.controller;


import cn.hutool.core.bean.BeanUtil;
import com.Surnia.articleService.entity.CmsArticle;
import com.Surnia.articleService.entity.CmsArticleSort;
import com.Surnia.articleService.entity.dto.LessonDTO;
import com.Surnia.articleService.entity.edu.EduVideo;
import com.Surnia.articleService.entity.vo.ArticleDetails;
import com.Surnia.articleService.entity.vo.ArticleWrapper;
import com.Surnia.articleService.feign.EduFeignService;
import com.Surnia.articleService.feign.UserFeignService;
import com.Surnia.articleService.service.CmsArticleService;
import com.Surnia.articleService.service.CmsArticleSortService;
import com.Surnia.commons.utils.GuLiUtils;
import com.Surnia.commons.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-09-14
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "文章系统内容管理")
@RestController
@RequestMapping("/articleService/content")
public class CmsArticleController {
    @Resource
    private CmsArticleService cmsArticleService;
    @Resource
    private CmsArticleSortService cmsArticleSortService;
    @Resource
    private UserFeignService userFeignService;
    @Resource
    private EduFeignService eduFeignService;
    @Resource
    private CmsArticleSortController cmsArticleSortController;




    @ApiOperation(value = "根据id获取文章所有信息")
    @GetMapping("getArticleById/{articleId}")
    public Result getArticleById(@PathVariable("articleId") String articleId) {

        CmsArticle article = cmsArticleService.getById(articleId);
        if (article != null) {
            ArticleDetails articleDetails = BeanUtil.toBean(article, ArticleDetails.class);

            HashMap<String, Object> user = userFeignService.getUserById(article.getUserId());
            articleDetails.setUserName((String) user.get("userNickname"));
            articleDetails.setUserAvatar((String) user.get("userAvatar"));

            CmsArticleSort articleSort = cmsArticleSortService.getById(articleDetails.getSortId());
            articleDetails.setSortName(articleSort.getSortName());
            articleDetails.setSubjectId(articleSort.getSubjectId());
            articleDetails.setSubjectTitle(articleSort.getSubjectName());
            articleDetails.setCourseId(articleSort.getCourseId());
            String courseName = eduFeignService.getCourseNameByCourseId(articleDetails.getCourseId());
            articleDetails.setCourseName(courseName);
            articleDetails.setChapterId(articleSort.getChapterId());
            String chapterName = eduFeignService.getChapterNameByChapterId(articleDetails.getChapterId());
            articleDetails.setChapterName(chapterName);
            articleDetails.setVideoId(articleSort.getVideoId());
            String videoName = eduFeignService.getVideoNameByVideoId(articleDetails.getVideoId());
            articleDetails.setVideoName(videoName);

            return Result.ok().data("article", articleDetails);
        } else {
            return Result.error().message("该文章不存在");
        }
    }

    @ApiOperation(value = "根据LessonDTO查询课程笔记列表")
    @PostMapping("getArticleListByLessonDTO")
    public Result getArticleListByLessonDTO(@RequestBody LessonDTO lessonDTO){

        String sortId = cmsArticleSortController.getSortIdByLessonDTO(lessonDTO);
        System.out.println(sortId);
        if ("UNWANTED".equals(sortId) || "NOTFOUND".equals(sortId)){
            return Result.error().message("暂时没有相关课程笔记");
        }

        QueryWrapper<CmsArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sort_id", sortId);

        List<CmsArticle> articleList = cmsArticleService.list(queryWrapper);
        System.out.println(articleList.size());
        if (articleList.size() > 0){
            return Result.ok().data("articleList",articleList);
        }
        return Result.error().message("暂时没有相关课程笔记");
    }

    @ApiOperation(value = "多条件查询分页获取所有文章")
    @PostMapping("getArticlePageByWrapper/{page}/{size}")
    public Result getArticlePageByWrapper(@PathVariable("page") long current
            , @PathVariable("size") long size
            , @RequestBody(required = false) ArticleWrapper wrapper) {
        System.out.println(wrapper);

        // 构建分页对象
        Page<CmsArticle> page = new Page<>(current, size);
        // 构建查询条件
        QueryWrapper<CmsArticle> articleQueryWrapper = new QueryWrapper<>();

        // 判断多条件组合查询是否为空，不为空才进行查询条件的具体构建
        if (!GuLiUtils.checkObjFieldAllNull(wrapper)) {
            // 根据关键字进行查询条件构造
            if (wrapper.getTitle() != null) {
                // 构造查询条件：模糊查询标题中含有关键字的文章
                articleQueryWrapper.like("title", wrapper.getWrapperKeyword());
            }
            if (wrapper.getSortName() != null) {
                // 根据关键字模糊查询分类名，根据分类名查询分类id
                QueryWrapper<CmsArticleSort> articleSortQueryWrapper = new QueryWrapper<>();
                articleSortQueryWrapper.like("sort_name", wrapper.getWrapperKeyword());
                CmsArticleSort articleSort = cmsArticleSortService.getOne(articleSortQueryWrapper);

                if (articleSort != null) {
                    // 构造查询条件：模糊查询分类名中含有关键字的文章
                    articleQueryWrapper.eq("sort_id", articleSort);
                } else {
                    return Result.error().message("根据关键词暂无该类文章分类搜索结果");
                }
            }
            if (wrapper.getContent() != null) {
                // 构造查询条件：模糊查询文章正文中含有关键字的文章，搜md格式比搜html格式要快一些
                articleQueryWrapper.like("remark", wrapper.getWrapperKeyword());
            }
            // 根据排序项进行查询条件构造 可以改进抽象 todo
            if (wrapper.getRemake() != null) {
                if ("likeCountDesc".equals(wrapper.getRemake())) {
                    articleQueryWrapper.orderByDesc("like_count");
                }
                if ("likeCountAsc".equals(wrapper.getRemake())) {
                    articleQueryWrapper.orderByAsc("like_count");
                }
                if ("commentCountDesc".equals(wrapper.getRemake())) {
                    articleQueryWrapper.orderByDesc("comment_count");
                }
                if ("commentCountAsc".equals(wrapper.getRemake())) {
                    articleQueryWrapper.orderByDesc("comment_count");
                }
                if ("timeSortDesc".equals(wrapper.getRemake())) {
                    articleQueryWrapper.orderByDesc("gmt_modified");
                }
                if ("timeSortAsc".equals(wrapper.getRemake())) {
                    articleQueryWrapper.orderByDesc("gmt_modified");
                }
            }
        }
        // 文章不能是未发布或者已逻辑删除
        articleQueryWrapper.notIn("is_publish", 0);
        articleQueryWrapper.notIn("is_deleted", 1);
        // 利用分页对象和查询条件查出所需要的分页信息
        IPage<CmsArticle> resultPage = cmsArticleService.page(page, articleQueryWrapper);

        // 更新查询文章作者名
        List<CmsArticle> articles = resultPage.getRecords();
        for (CmsArticle article : articles) {
            HashMap<String, Object> user = userFeignService.getUserById(article.getUserId());
            article.setUserName((String) user.get("userNickname"));
        }
        resultPage.setRecords(articles);

        return Result.ok().data("resultPage", resultPage);
    }

    //@ApiOperation(value = "根据用户id分页获取其所有文章")

    @ApiOperation(value = "增加文章")
    @PostMapping("addArticle")
    // lessonNo:0为没有此类id，1为courseId，2为chapterId，3为videoId，
    public Result addArticle(@RequestBody CmsArticle article) {
        System.out.println("add...");
        System.out.println("article..." + article);


        HashMap<String, Object> user = userFeignService.getUserById(article.getUserId());
        article.setUserName((String) user.get("userNickname"));
        System.out.println("article..." + article);
        boolean result = cmsArticleService.save(article);
        System.out.println("result..." + result);
        if (result) {
            return Result.ok().data("articleId", article.getId());
        } else {
            return Result.error();
        }

    }

    @ApiOperation(value = "修改文章")
    @PutMapping("updateArticle")
    public Result updateArticle(@RequestBody CmsArticle article) {
        System.out.println("------------id:" + article.getId());
        QueryWrapper<CmsArticle> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("id", article.getId());
        boolean result = cmsArticleService.update(article, articleQueryWrapper);
        if (result) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "根据id删除文章")
    @DeleteMapping("deleteArticle/{articleId}")
    public Result deleteArticle(@PathVariable("articleId") String articleId) {
        boolean result = cmsArticleService.removeById(articleId);
        if (result) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    //@ApiOperation(value = "文章浏览量增加")

    //@ApiOperation(value = "文章点赞数增加")
    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据文章id点赞")
    @PostMapping("addArticleLike/{articleId}")
    public boolean addArticleLike(@PathVariable("articleId") String articleId){
        CmsArticle cmsArticle = cmsArticleService.getById(articleId);
        if(cmsArticle == null){
            return false;
        }
        cmsArticle.setLikeCount(cmsArticle.getLikeCount() + 1);
        return cmsArticleService.updateById(cmsArticle);
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据文章id取消点赞")
    @DeleteMapping("deleteArticleLike/{articleId}")
    public boolean deleteArticleLike(@PathVariable("articleId") String articleId){
        CmsArticle cmsArticle = cmsArticleService.getById(articleId);
        if(cmsArticle == null){
            return false;
        }
        cmsArticle.setLikeCount(cmsArticle.getLikeCount() - 1);
        return cmsArticleService.updateById(cmsArticle);
    }
}

