package com.Surnia.articleService.feign;


import com.Surnia.articleService.entity.edu.EduCourse;
import com.Surnia.articleService.entity.edu.EduVideo;
import com.Surnia.commons.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@FeignClient(value = "service-edu")
public interface EduFeignService {

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据小节id查询小节所属章节id")
    @GetMapping("/eduService/video/getChapterIdByVideoId/{id}")
    public String getChapterIdByVideoId(@PathVariable("id") String videoId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据小节id查询小节名称")
    @GetMapping("/eduService/video/getVideoNameByVideoId/{id}")
    public String getVideoNameByVideoId(@PathVariable("id") String videoId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据章节id查询小节所属课程id")
    @GetMapping("/eduService/chapter/getCourseIdByChapterId/{id}")
    public String getCourseIdByChapterId(@PathVariable("id") String chapterId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据章节id查询章节名称")
    @GetMapping("/eduService/chapter/getChapterNameByChapterId/{id}")
    public String getChapterNameByChapterId(@PathVariable("id") String chapterId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id查询学科信息")
    @GetMapping("/eduService/course/getSubjectInfoById/{id}")
    public HashMap<String,String> getSubjectInfoById(@PathVariable("id") String courseId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id查询课程名称")
    @GetMapping("/eduService/course/getCourseNameByCourseId/{id}")
    public String getCourseNameByCourseId(@PathVariable("id") String courseId);


    // 评论功能相关


    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id查询课程")
    @GetMapping("/eduService/course/getCourseByCourseId/{id}")
    public EduCourse getCourseByCourseId(@PathVariable("id") String courseId);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程对象修改课程")
    @PutMapping("/eduService/course/updateCourseByCourse")
    public boolean updateCourseByCourse(@RequestBody EduCourse eduCourse);

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据视频id查询视频")
    @GetMapping("/eduService/video/getVideoByVideoId/{id}")
    public EduVideo getVideoByVideoId(@PathVariable("id") String videoId);


    @ApiOperation(value = "修改小节")
    @PutMapping("/eduService/video/updateVideo")
    public Result updateVideo(@RequestBody EduVideo eduVideo);
}
