package com.Surnia.eduService.controller;


import com.Surnia.commons.utils.Result;
import com.Surnia.eduService.entity.EduChapter;
import com.Surnia.eduService.entity.EduVideo;
import com.Surnia.eduService.feign.AliyunVodFeignService;
import com.Surnia.eduService.service.EduChapterService;
import com.Surnia.eduService.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-08
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "章节管理")
@RestController
@RequestMapping("/eduService/chapter")
public class EduChapterController {
    @Resource
    private EduChapterService eduChapterService;
    @Resource
    private EduVideoService eduVideoService;

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据章节id查询小节所属课程id")
    @GetMapping("getCourseIdByChapterId/{id}")
    public String getCourseIdByChapterId(@PathVariable("id") String chapterId){
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return eduChapter.getCourseId();
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据章节id查询章节名称")
    @GetMapping("getChapterNameByChapterId/{id}")
    public String getChapterNameByChapterId(@PathVariable("id") String chapterId){
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        if (eduChapter != null){
            return eduChapter.getTitle();
        }
        return "non-existent";
    }


    @ApiOperation(value = "根据课程id查询所有章节")
    @GetMapping("getChapterByCourseId/{id}")
    public Result getChapterByCourseId(@PathVariable("id") String courseId) {
        List<EduChapter> list = eduChapterService.getChapterByCourseId(courseId);
        return Result.ok().data("chapterList", list);
    }

    @ApiOperation(value = "新增章节")
    @PostMapping("createChapter")
    public Result createChapter(@RequestBody EduChapter eduChapter) {
        boolean result = eduChapterService.save(eduChapter);
        if (result) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "删除章节")
    @DeleteMapping("deleteChapter/{id}")
    public Result deleteChapter(@PathVariable("id") String chapterId) {
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("chapter_id", chapterId);
        List<EduVideo> videoList = eduVideoService.list(eduVideoQueryWrapper);
        if (videoList.isEmpty()) {
            QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
            eduChapterQueryWrapper.eq("id", chapterId);
            boolean result = eduChapterService.remove(eduChapterQueryWrapper);
            if (result) {
                return Result.ok();
            } else {
                return Result.error();
            }
        } else {
            throw new RuntimeException("清空章节包含的小节前不能删除该章节");
        }
    }

    @ApiOperation(value = "修改章节")
    @PutMapping("updateChapter")
    public Result updateChapter(@RequestBody EduChapter eduChapter){
        boolean result = eduChapterService.updateById(eduChapter);
        if (result) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

}

