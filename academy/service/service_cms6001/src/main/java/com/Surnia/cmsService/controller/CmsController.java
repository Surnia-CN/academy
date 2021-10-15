package com.Surnia.cmsService.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.Surnia.cmsService.entity.Chapter;
import com.Surnia.cmsService.entity.Video;
import com.Surnia.cmsService.entity.dto.LessonDTO;
import com.Surnia.cmsService.entity.vo.CourseInfoVo;
import com.Surnia.cmsService.entity.vo.EduCourseInfo;
import com.Surnia.cmsService.feign.ArticleFeignService;
import com.Surnia.cmsService.feign.EduFeignService;
import com.Surnia.commons.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CmsController
 * @Description TODO
 * @Author Surnia
 * @Date 2021/8/31
 * @Version 1.0
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "前台系统内容管理")
@RestController
@RequestMapping("/cmsService/content")
public class CmsController {

    @Resource
    private EduFeignService eduFeignService;
    @Resource
    private ArticleFeignService articleFeignService;


    @ApiOperation(value = "根据小节id查询小节对应课程的小节列表")
    @GetMapping("getVideoListByVideoId/{videoId}")
    public Result getVideoListByVideoId(@PathVariable("videoId") String videoId){
        return eduFeignService.getVideoListByVideoId(videoId);
    }

    @ApiOperation(value = "根据小节id查询小节播放页的相关信息")
    @GetMapping("getVideoInfoByVideoId/{videoId}")
    // 课程名称、视频名称、课程分类、更新时间
    public Result getVideoInfoByVideoId(@PathVariable("videoId") String videoId){
        return eduFeignService.getVideoInfoByVideoId(videoId);
    }

    @ApiOperation(value = "根据小节id查询小节相关课程笔记")
    @GetMapping("getArticleListByVideoId/{videoId}")
    public Result getArticleListByVideoId(@PathVariable("videoId") String videoId){
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setLessonNo("3");
        lessonDTO.setLessonId(videoId);
        System.out.println(lessonDTO);
        return articleFeignService.getArticleListByLessonDTO(lessonDTO);
    }

    @ApiOperation(value = "分页查询所有讲师")
    @GetMapping("getAllTeacherPage/{page}/{size}")
    public Result getAllTeacherPage(@PathVariable("page") long current, @PathVariable("size") long size) {
        System.out.println("query...");
        return eduFeignService.getAllTeacherPage(current, size);
    }

    @ApiOperation(value = "按讲师id查询讲师信息")
    @GetMapping("getTeacherById/{teacherId}")
    public Result getTeacherById(@PathVariable("teacherId") long teacherId) {
        return eduFeignService.getTeacher(teacherId);
    }

    @ApiOperation(value = "按讲师id查询其主讲课程")
    @GetMapping("getCourseListByTeacherId/{teacherId}")
    public Result getCourseListByTeacherId(@PathVariable("teacherId") String teacherId) {
        return eduFeignService.getCourseListByTeacherId(teacherId);
    }

    @ApiOperation(value = "获取学科菜单列表")
    @GetMapping("getSubjectList")
    public Result getSubjectList(){
        return eduFeignService.getAllSubject();
    }

    @ApiOperation(value = "多条件组合分页查询所有课程")
    @PostMapping("getAllCoursePage/{page}/{size}")
    public Result getAllCoursePage(@PathVariable("page") long current
            , @PathVariable("size") long size
            , @RequestBody(required = false) EduCourseInfo queryWrapper) {
        return eduFeignService.getAllCoursePage(current, size, queryWrapper);
    }


    @ApiOperation(value = "查询课程详情")
    @GetMapping("getCourseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable("courseId") String courseId) {

        // 通过课程id查询出课程信息
        Result courseInfoById = eduFeignService.getCourseInfoById(courseId);
        Map<String,Object> eduCourseInfo = (Map<String, Object>) courseInfoById.getData().get("eduCourseInfo");

        // 将map转换并存入vo对象中
        CourseInfoVo courseInfoVo = BeanUtil.toBean(eduCourseInfo,CourseInfoVo.class);
        System.out.println("--------courseInfoVo---------:" +courseInfoVo);

        // 通过课程id查询出课程的章节大纲
        Result chapterByCourseId = eduFeignService.getChapterByCourseId(courseId);
        List<Chapter> chapterList = (List<Chapter>) chapterByCourseId.getData().get("chapterList");

        // 将Result中的章节大纲转换为JSONArray，再转回List<Chapter>
        /* 避免报错java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class com.Surnia.cmsService.entity.Chapter
         (java.util.LinkedHashMap is in module java.base of loader 'bootstrap';
         com.Surnia.cmsService.entity.Chapter is in unnamed module of loader 'app')*/
        String jsonStr = JSONUtil.toJsonStr(chapterList);
        JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
        chapterList = JSONUtil.toList(jsonArray, Chapter.class);

        // 存入vo对象中
        courseInfoVo.setChapterList(chapterList);
        System.out.println("--------courseInfoVo---------:" +courseInfoVo);

        // 通过课程id查询出课程的小节信息
        Result videoByCourseId = eduFeignService.getVideoByCourseId(courseId);
        List<Video> videoList = (List<Video>) videoByCourseId.getData().get("videoList");

        // 将Result中的小节信息转换为JSONArray，再转回List<Video>
        jsonStr = JSONUtil.toJsonStr(videoList);
        jsonArray = JSONUtil.parseArray(jsonStr);
        videoList = JSONUtil.toList(jsonArray, Video.class);

        // 将小节信息存入vo对象对应的章节中
        for (Chapter chapter : courseInfoVo.getChapterList()) {
            chapter.setVideoList(new ArrayList<>());
            System.out.println("----- chapter:" + chapter);
            for (Video video : videoList) {
                if (chapter.getId().equals(video.getChapterId())) {
                    chapter.getVideoList().add(video);
                }
            }
        }
        System.out.println("--------courseInfoVo---------:" +courseInfoVo);

        // 通过课程id查询出课程的课程分类名
        Result subjectNameById = eduFeignService.getSubjectNameById(courseInfoVo.getSubjectId());
        System.out.println("subjectNameById:" + subjectNameById);
        String subjectName = (String) subjectNameById.getData().get("subjectName");
        courseInfoVo.setSubjectName(subjectName);
        Result subjectParentNameById = eduFeignService.getSubjectNameById(courseInfoVo.getSubjectParentId());
        System.out.println("subjectParentNameById:" + subjectParentNameById);
        String subjectParentName = (String) subjectParentNameById.getData().get("subjectName");
        courseInfoVo.setSubjectParentName(subjectParentName);
        System.out.println("--------courseInfoVo---------:" +courseInfoVo);

        return Result.ok().data("courseDetails", courseInfoVo);
    }

}
