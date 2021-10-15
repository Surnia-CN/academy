package com.Surnia.cmsService.feign;


import com.Surnia.cmsService.entity.vo.EduCourseInfo;
import com.Surnia.commons.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "service-edu")
public interface EduFeignService {

    // 以课程阅读量和最新修改时间为依据
    @ApiOperation(value = "查询热门课程(8门)")
    @GetMapping("/eduService/course/getHotCourse")
    Result getHotCourse();

    // 以首席讲师和排序为依据
    @ApiOperation(value = "查询名师(4位)")
    @GetMapping("/eduService/teacher/getHotTeacher")
    Result getHotTeacher();

    @ApiOperation(value = "查询所有课程分类")
    @GetMapping("/eduService/subject/getAllSubject")
    public Result getAllSubject();


    @ApiOperation(value = "多条件组合分页查询所有课程")
    @PostMapping("/eduService/course/getCoursePageByWrapper/{page}/{size}")
    public Result getAllCoursePage(@PathVariable("page") long current
            , @PathVariable("size") long size
            , @RequestBody(required = false) EduCourseInfo queryWrapper);



    @ApiOperation(value = "分页查询所有讲师")
    @GetMapping("/eduService/teacher/getAllByPage/{page}/{size}")
    public Result getAllTeacherPage(@PathVariable("page") long current, @PathVariable("size") long size);

    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("/eduService/teacher/get/{id}")
    public Result getTeacher(@PathVariable("id") long id);

    @ApiOperation(value = "按讲师id查询其主讲课程")
    @GetMapping("/eduService/course/getCourseListByTeacherId/{teacherId}")
    public Result getCourseListByTeacherId(@PathVariable("teacherId") String teacherId);

    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping("/eduService/course/getCourseInfoById/{id}")
    public Result getCourseInfoById(@PathVariable("id") String courseId);

    @ApiOperation(value = "根据课程id查询所有章节")
    @GetMapping("/eduService/chapter/getChapterByCourseId/{id}")
    public Result getChapterByCourseId(@PathVariable("id") String courseId);

    @ApiOperation(value = "根据课程id查询所有小节")
    @GetMapping("/eduService/video/getVideoByCourseId/{id}")
    public Result getVideoByCourseId(@PathVariable("id") String courseId);

    @ApiOperation(value = "根据课程分类id查询课程分类名")
    @GetMapping("/eduService/subject/getSubjectNameById/{subjectId}")
    public Result getSubjectNameById(@PathVariable("subjectId")String subjectId);


    @ApiOperation(value = "根据课程id获取视频id")
    @GetMapping("/eduService/video/getVidByVideoId")
    public Result getVidByVideoId(@RequestParam("videoId") String videoId);


    @ApiOperation(value = "根据小节id查询小节对应课程的小节列表")
    @GetMapping("/eduService/video/getVideoListByVideoId/{videoId}")
    public Result getVideoListByVideoId(@PathVariable("videoId") String videoId);

    @ApiOperation(value = "根据小节id查询小节播放页的相关信息")
    @GetMapping("/eduService/video/getVideoInfoByVideoId/{videoId}")
    // 课程名称、视频名称、课程分类、更新时间、
    public Result getVideoInfoByVideoId(@PathVariable("videoId") String videoId);
}
