package com.Surnia.articleService.controller;


import com.Surnia.articleService.entity.CmsArticleSort;
import com.Surnia.articleService.entity.dto.LessonDTO;
import com.Surnia.articleService.feign.EduFeignService;
import com.Surnia.articleService.mapper.CmsArticleSortMapper;
import com.Surnia.articleService.service.CmsArticleSortService;
import com.Surnia.commons.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-09-14
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "文章系统分类管理")
@RestController
@RequestMapping("/articleService/sort")
public class CmsArticleSortController {

    @Resource
    private CmsArticleSortMapper cmsArticleSortMapper;
    @Resource
    private EduFeignService eduFeignService;

    @ApiOperation(value = "获取所有文章分类")
    @GetMapping("getAllSort")
    public Result getAllSort(){
        //List<CmsArticleSort> sortList = cmsArticleSortService.list(null);
        List<CmsArticleSort> sortList = cmsArticleSortMapper.selectList(null);
        return Result.ok().data("sortList", sortList);
    }

    @ApiOperation(value = "根据分类id获取文章分类")
    @GetMapping("getAllSort/{sortId}")
    public Result getSortById(@PathVariable("sortId") String sortId){
        //CmsArticleSort articleSort = cmsArticleSortService.getById(sortId);
        CmsArticleSort articleSort = cmsArticleSortMapper.selectById(sortId);
        if (articleSort != null){
            return Result.ok().data("articleSort",articleSort);
        } else {
            return Result.error().message("该文章分类不存在");
        }
    }

    //@ApiOperation(value = "根据分类id分页获取文章")

    @ApiOperation(value = "根据lesson获取文章分类id，不存在则新增")
    @PostMapping("getOrAddSortIdByLesson")
    public Result getOrAddSortIdByLesson(@RequestBody LessonDTO lessonDTO){
        // 判断sortId是否存在
        String sortId = getSortIdByLessonDTO(lessonDTO);
        // 不存在则创建sort，返回sortId，并将sortId存入article对象中
        if("NOTFOUND".equals(sortId)){
            Result result = addSortByLesson(lessonDTO);
            sortId = (String) result.getData().get("sortId");
            if (!"Fail".equals(sortId)){
                return Result.ok().data("sortId", sortId);
            }
        }
        return Result.ok().data("sortId", sortId);
    }

    // 内部接口
    @ApiOperation(value = "根据lessonDTO获取文章分类id")
    @PostMapping("getSortIdByLessonDTO")
    public String getSortIdByLessonDTO(@RequestBody LessonDTO lessonDTO){
        System.out.println(lessonDTO);

        String lessonNo = lessonDTO.getLessonNo();
        String lessonId = lessonDTO.getLessonId();
        CmsArticleSort cmsArticleSort = new CmsArticleSort();
        //
        cmsArticleSort.setId("UNWANTED");
        QueryWrapper<CmsArticleSort> queryWrapper = new QueryWrapper<>();
        // lessonNo为0则不进入，直接返回“UNWANTED”
        if(!"0".equals(lessonNo)){
            if("3".equals(lessonNo)){
                queryWrapper.eq("video_id", lessonId);
                cmsArticleSort = cmsArticleSortMapper.selectOne(queryWrapper);
            }
            if("2".equals(lessonNo)){
                queryWrapper.eq("chapter_id", lessonId);
                queryWrapper.eq("video_id", "0");
                cmsArticleSort = cmsArticleSortMapper.selectOne(queryWrapper);
            }
            if("1".equals(lessonNo)){
                queryWrapper.eq("course_id", lessonId);
                queryWrapper.eq("video_id", "0");
                queryWrapper.eq("chapter_id", "0");
                cmsArticleSort = cmsArticleSortMapper.selectOne(queryWrapper);
            }
        }
        // lessonNo不为0，查询后结果为null，则返回“NOTFOUND”
        if (cmsArticleSort == null){
            return "NOTFOUND";
        }
        // lessonNo不为0，查询成功的情况返回实际id
        return cmsArticleSort.getId();

    }

    @ApiOperation(value = "增加文章分类")
    @PostMapping("addSort")
    public Result addSort(@RequestBody CmsArticleSort sort){
        //boolean result = cmsArticleSortService.save(sort);
        int result = cmsArticleSortMapper.insert(sort);
        if (result == 1) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "根据视频增加文章分类")
    @PostMapping("addSortByLesson")
    // lessonNo:0为没有此类id，1为courseId，2为chapterId，3为videoId，
    public Result addSortByLesson(@RequestBody LessonDTO lessonDTO){

        String lessonNo = lessonDTO.getLessonNo();
        String lessonId = lessonDTO.getLessonId();

        CmsArticleSort sort = new CmsArticleSort();
        if(!"0".equals(lessonNo)){
            if("3".equals(lessonNo)){
                sort.setVideoId(lessonId);
                // 根据videoId查询chapterId
                String chapterId = eduFeignService.getChapterIdByVideoId(lessonId);
                lessonNo = "2";
                lessonId = chapterId;
            }
            if("2".equals(lessonNo)){
                sort.setChapterId(lessonId);
                // 根据chapterId查询courseId
                String courseId = eduFeignService.getCourseIdByChapterId(lessonId);
                lessonNo = "1";
                lessonId = courseId;
            }
            if("1".equals(lessonNo)){
                sort.setCourseId(lessonId);
                // 根据courseId查询学科id和学科名
                HashMap<String, String> subjectInfo = eduFeignService.getSubjectInfoById(lessonId);
                sort.setSubjectId(subjectInfo.get("subjectId"));
                sort.setSubjectName(subjectInfo.get("subjectName"));
                sort.setSortName(subjectInfo.get("subjectName"));
            }
        }

        //boolean result = cmsArticleSortService.save(sort);
        int result = cmsArticleSortMapper.insert(sort);
        if (result == 1) {
            return Result.ok().data("sortId", sort.getId());
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "修改章节")
    @PutMapping("updateSort")
    public Result updateSort(@RequestBody CmsArticleSort sort){
        //boolean result = cmsArticleSortService.updateById(sort);
        QueryWrapper<CmsArticleSort> wrapper = new QueryWrapper<>();
        wrapper.eq("id", sort.getId());
        int result = cmsArticleSortMapper.update(sort, wrapper);
        if (result == 1) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "根据分类id删除文章分类")
    @DeleteMapping("deleteSort/{sortId}")
    public Result deleteSort(@PathVariable("sortId") String sortId){

        int result = cmsArticleSortMapper.deleteById(sortId);
        if (result == 1) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }
}

