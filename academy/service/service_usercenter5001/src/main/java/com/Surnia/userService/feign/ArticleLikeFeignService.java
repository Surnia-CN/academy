package com.Surnia.userService.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "service-article")
public interface ArticleLikeFeignService {
    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据文章id点赞")
    @PostMapping("/articleService/content/addArticleLike/{articleId}")
    public boolean addArticleLike(@PathVariable("articleId") String articleId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据视频id取消点赞")
    @DeleteMapping("/articleService/content/deleteArticleLike/{articleId}")
    public boolean deleteArticleLike(@PathVariable("articleId") String articleId);


    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据文章id点赞")
    @PostMapping("/articleService/comment/addCommentLike/{commentId}")
    public boolean addCommentLike(@PathVariable("commentId") String commentId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据文章id取消点赞")
    @DeleteMapping("/articleService/comment/deleteCommentLike/{commentId}")
    public boolean deleteCommentLike(@PathVariable("commentId") String commentId);

}
