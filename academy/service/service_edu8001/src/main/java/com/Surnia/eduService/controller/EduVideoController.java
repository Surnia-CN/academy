package com.Surnia.eduService.controller;


import cn.hutool.core.bean.BeanUtil;
import com.Surnia.commons.utils.Result;
import com.Surnia.eduService.entity.EduCourse;
import com.Surnia.eduService.entity.EduSubject;
import com.Surnia.eduService.entity.EduVideo;
import com.Surnia.eduService.entity.vo.EduVideoInfoVO;
import com.Surnia.eduService.feign.AliyunVodFeignService;
import com.Surnia.eduService.service.EduCourseService;
import com.Surnia.eduService.service.EduSubjectService;
import com.Surnia.eduService.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-08
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "小节管理")
@RestController
@RequestMapping("/eduService/video")
public class EduVideoController {

    @Resource
    private EduVideoService eduVideoService;
    @Resource
    private AliyunVodFeignService aliyunVodFeignService;
    @Resource
    private EduCourseService eduCourseService;
    @Resource
    private EduSubjectService eduSubjectService;

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据视频id查询视频")
    @GetMapping("getVideoByVideoId/{id}")
    public EduVideo getVideoByVideoId(@PathVariable("id") String videoId){
        return eduVideoService.getById(videoId);
    }


    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据小节id查询小节所属章节id")
    @GetMapping("getChapterIdByVideoId/{id}")
    public String getChapterIdByVideoId(@PathVariable("id") String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return eduVideo.getChapterId();
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据小节id查询小节名称")
    @GetMapping("getVideoNameByVideoId/{id}")
    public String getVideoNameByVideoId(@PathVariable("id") String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        if (eduVideo != null){
            return eduVideo.getTitle();
        }
        return "non-existent";
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据视频id点赞")
    @PostMapping("addVideoLike/{videoId}")
    public boolean addVideoLike(@PathVariable("videoId") String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        if(eduVideo == null){
            return false;
        }
        eduVideo.setLikeCount(eduVideo.getLikeCount() + 1);
        return eduVideoService.updateById(eduVideo);
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据视频id取消点赞")
    @DeleteMapping("deleteVideoLike/{videoId}")
    public boolean deleteVideoLike(@PathVariable("videoId") String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        if(eduVideo == null){
            return false;
        }
        eduVideo.setLikeCount(eduVideo.getLikeCount() - 1);
        return eduVideoService.updateById(eduVideo);
    }

    @ApiOperation(value = "根据小节id查询小节播放页的相关信息")
    @GetMapping("getVideoInfoByVideoId/{videoId}")
    // 课程名称、视频名称、课程分类、更新时间、
    public Result getVideoInfoByVideoId(@PathVariable("videoId") String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        EduCourse eduCourse = eduCourseService.getById(eduVideo.getCourseId());

        EduVideoInfoVO eduVideoInfoVO = BeanUtil.toBean(eduVideo, EduVideoInfoVO.class);

        eduVideoInfoVO.setCourseName(eduCourse.getTitle());
        eduVideoInfoVO.setSubjectId(eduCourse.getSubjectId());
        eduVideoInfoVO.setSubjectParentId(eduCourse.getSubjectParentId());

        EduSubject subject = eduSubjectService.getById(eduCourse.getSubjectId());
        eduVideoInfoVO.setSubjectName(subject.getTitle());
        subject = eduSubjectService.getById(eduCourse.getSubjectParentId());
        eduVideoInfoVO.setSubjectParentName(subject.getTitle());

        System.out.println(eduVideoInfoVO);

        return Result.ok().data("videoInfo", eduVideoInfoVO);
    }


    @ApiOperation(value = "根据小节id查询小节对应课程的小节列表")
    @GetMapping("getVideoListByVideoId/{videoId}")
    public Result getVideoListByVideoId(@PathVariable("videoId") String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        String courseId = eduVideo.getCourseId();
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(queryWrapper);
        return Result.ok().data("videoList", eduVideoList);
    }

    @ApiOperation(value = "根据课程id查询所有小节")
    @GetMapping("getVideoByCourseId/{id}")
    public Result getVideoByCourseId(@PathVariable("id") String courseId){
        List<EduVideo> list = eduVideoService.getVideoByCourseId(courseId);
        return Result.ok().data("videoList", list);
    }

    @ApiOperation(value = "根据课程id获取视频id")
    @GetMapping("getVidByVideoId")
    public Result getVidByVideoId(String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return Result.ok().data("vid",eduVideo.getVideoSourceId());
    }

    @ApiOperation(value = "新增小节")
    @PostMapping("createVideo")
    public Result createVideo(@RequestBody EduVideo eduVideo) {
        boolean result = eduVideoService.save(eduVideo);
        if (result) {
            // 总课时+1
            EduCourse course = eduCourseService.getById(eduVideo.getCourseId());
            course.setLessonNum(course.getLessonNum() + 1);
            eduCourseService.updateById(course);
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "删除小节，并同步删除阿里云vod中的视频")
    @DeleteMapping("deleteVideo/{id}")
    public Result deleteVideo(@PathVariable("id") String videoId) {
        // 通过videoId查询到video数据
        EduVideo video = eduVideoService.getById(videoId);
        // video数据中存在阿里云vod视频id时同步删除
        boolean vodResult = true;
        if(video.getVideoSourceId() != null){
            // 远程调用9001服务删除阿里云vod的视频
            vodResult = aliyunVodFeignService.deleteVod(video.getVideoSourceId());
        }

        if (vodResult){
            // 查询video对应的课程
            EduCourse course = eduCourseService.getById(video.getCourseId());
            // 删除video
            QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
            eduVideoQueryWrapper.eq("id", videoId);
            boolean removeResult = eduVideoService.remove(eduVideoQueryWrapper);
            if (removeResult) {
                // 总课时-1
                course.setLessonNum(course.getLessonNum() + 1);
                eduCourseService.updateById(course);
                return Result.ok();
            } else {
                return Result.error().message("删除数据库小节信息失败");
            }
        } else {
            return Result.error().message("删除阿里云vod视频失败");
        }
    }

    @ApiOperation(value = "修改小节")
    @PutMapping("updateVideo")
    public Result updateVideo(@RequestBody EduVideo eduVideo){
        boolean result = eduVideoService.updateById(eduVideo);
        if (result) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }
}

