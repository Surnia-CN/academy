package com.Surnia.cmsService.feign;

import com.Surnia.cmsService.entity.dto.LessonDTO;
import com.Surnia.commons.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-article")
public interface ArticleFeignService {

    @ApiOperation(value = "根据LessonDTO查询课程笔记列表")
    @PostMapping("/articleService/content/getArticleListByLessonDTO")
    public Result getArticleListByLessonDTO(@RequestBody LessonDTO lessonDTO);
}
