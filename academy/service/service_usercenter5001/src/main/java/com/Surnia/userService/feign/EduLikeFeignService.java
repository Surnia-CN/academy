package com.Surnia.userService.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "service-edu")
public interface EduLikeFeignService {

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id为课程点赞")
    @PostMapping("/eduService/course/addCourseLike/{courseId}")
    public boolean addCourseLike(@PathVariable("courseId") String courseId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id为课程取消点赞")
    @DeleteMapping("/eduService/course/deleteCourseLike/{courseId}")
    public boolean deleteCourseLike(@PathVariable("courseId") String courseId);


    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据视频id点赞")
    @PostMapping("/eduService/video/addVideoLike/{videoId}")
    public boolean addVideoLike(@PathVariable("videoId") String videoId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据视频id取消点赞")
    @DeleteMapping("/eduService/video/deleteVideoLike/{videoId}")
    public boolean deleteVideoLike(@PathVariable("videoId") String videoId);
}
