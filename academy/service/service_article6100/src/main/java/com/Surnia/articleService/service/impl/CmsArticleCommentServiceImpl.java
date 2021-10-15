package com.Surnia.articleService.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.Surnia.articleService.entity.CmsArticleComment;
import com.Surnia.articleService.entity.dto.CommentDTO;
import com.Surnia.articleService.mapper.CmsArticleCommentMapper;
import com.Surnia.articleService.service.CmsArticleCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-09-14
 */
@Service
public class CmsArticleCommentServiceImpl extends ServiceImpl<CmsArticleCommentMapper, CmsArticleComment> implements CmsArticleCommentService {

    @Override
    public boolean addComment(CommentDTO commentDTO) {

        System.out.println(commentDTO);

        if (commentDTO.getParentId() == null) {
            commentDTO.setParentId("0");
        }

        CmsArticleComment comment = BeanUtil.toBean(commentDTO, CmsArticleComment.class);

        comment.setLikeCount((long) 0);
        comment.setUnlikeCount((long) 0);
        comment.setCommentIndex((long) 0);
        comment.setChildrenIndex((long) 0);// 无用

        System.out.println(comment);
        // 评论不为子评论时，序号+1
        if ("0".equals(comment.getParentId())) {
            // 查询同主体id的父评论的条数，为当前条序号+1
            QueryWrapper<CmsArticleComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", 0);
            queryWrapper.eq("main_body_id", comment.getMainBodyId());
            Integer index = this.baseMapper.selectCount(queryWrapper);
            comment.setCommentIndex((long) (index + 1));
        }


        System.out.println(comment);
        int result = this.baseMapper.insert(comment);
        return result == 1;
    }
}
