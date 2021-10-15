package com.Surnia.articleService.service;

import com.Surnia.articleService.entity.CmsArticleComment;
import com.Surnia.articleService.entity.dto.CommentDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-09-14
 */
public interface CmsArticleCommentService extends IService<CmsArticleComment> {

    boolean addComment(CommentDTO commentDTO);
}
