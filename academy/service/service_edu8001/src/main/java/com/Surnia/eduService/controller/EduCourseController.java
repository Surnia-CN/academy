package com.Surnia.eduService.controller;


import cn.hutool.core.collection.CollUtil;
import com.Surnia.commons.utils.GuLiUtils;
import com.Surnia.commons.utils.Result;
import com.Surnia.eduService.entity.*;
import com.Surnia.eduService.entity.vo.EduCourseInfo;
import com.Surnia.eduService.entity.vo.EduTeacherQueryWrapper;
import com.Surnia.eduService.feign.AliyunVodFeignService;
import com.Surnia.eduService.service.EduChapterService;
import com.Surnia.eduService.service.EduCourseService;
import com.Surnia.eduService.service.EduSubjectService;
import com.Surnia.eduService.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-08
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "课程管理")
@RestController
@RequestMapping("/eduService/course")
public class EduCourseController {

    @Resource
    private EduCourseService eduCourseService;
    @Resource
    private EduChapterService eduChapterService;
    @Resource
    private EduVideoService eduVideoService;
    @Resource
    private AliyunVodFeignService aliyunVodFeignService;
    @Resource
    private EduSubjectService eduSubjectService;

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id查询课程")
    @GetMapping("getCourseByCourseId/{id}")
    public EduCourse getCourseByCourseId(@PathVariable("id") String courseId){
        return eduCourseService.getById(courseId);
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程对象修改课程")
    @PutMapping("updateCourseByCourse")
    public boolean updateCourseByCourse(@RequestBody EduCourse eduCourse){
        return eduCourseService.updateById(eduCourse);
    }


    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id查询课程名称")
    @GetMapping("getCourseNameByCourseId/{id}")
    public String getCourseNameByCourseId(@PathVariable("id") String courseId){
        EduCourse eduCourse = eduCourseService.getById(courseId);
        if (eduCourse != null){
            return eduCourse.getTitle();
        }
        return "non-existent";
    }

    @ApiOperation(value = "增加课程及简介")
    @PostMapping("addCourseInfo")
    public Result addCourseInfo(@RequestBody EduCourseInfo eduCourseInfo) {
        String courseId = eduCourseService.saveCourseInfo(eduCourseInfo);
        return Result.ok().data("courseId", courseId);
    }

    @ApiOperation(value = "根据课程id修改课程信息")
    @PutMapping("updateCourseInfoById")
    public Result updateCourseInfoById(@RequestBody EduCourseInfo eduCourseInfo) {
        String courseId = eduCourseService.updateCourseInfo(eduCourseInfo);
        return Result.ok().data("courseId", courseId);
    }


    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping("getCourseInfoById/{id}")
    public Result getCourseInfoById(@PathVariable("id") String courseId) {
        EduCourseInfo eduCourseInfo = eduCourseService.getCourseInfoById(courseId);
        System.out.println(eduCourseInfo);
        return Result.ok().data("eduCourseInfo", eduCourseInfo);
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id查询学科信息")
    @GetMapping("getSubjectInfoById/{id}")
    public HashMap<String,String> getSubjectInfoById(@PathVariable("id") String courseId) {
        EduCourseInfo eduCourseInfo = eduCourseService.getCourseInfoById(courseId);
        String subjectId = eduCourseInfo.getSubjectId();
        EduSubject eduSubject = eduSubjectService.getById(subjectId);
        HashMap<String,String> subjectMap = new HashMap<>();
        subjectMap.put("subjectId", eduSubject.getId());
        subjectMap.put("subjectName", eduSubject.getTitle());
        return subjectMap;
    }

    @ApiOperation(value = "按讲师id查询其主讲课程")
    @GetMapping("getCourseListByTeacherId/{teacherId}")
    public Result getCourseListByTeacherId(@PathVariable("teacherId") String teacherId) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", teacherId);
        List<EduCourse> courseList = eduCourseService.list(wrapper);
        return Result.ok().data("courseList", courseList);
    }

    // 以课程阅读量和最新修改时间为依据
    @ApiOperation(value = "查询热门课程(8门)")
    @GetMapping("getHotCourse")
    public Result getHotCourse(){
        List<EduCourse> list = eduCourseService.getHot();
        return Result.ok().data("hotCourseList", list);
    }

    @ApiOperation(value = "根据课程id发布课程")
    @PutMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable("id") String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        boolean result = eduCourseService.updateById(eduCourse);
        if (result) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "多条件组合分页查询所有课程")
    @PostMapping("getCoursePageByWrapper/{page}/{size}")
    public Result getCoursePageByWrapper(@PathVariable("page") long current
            , @PathVariable("size") long size
            , @RequestBody(required = false) EduCourseInfo queryWrapper) {

        System.out.println(queryWrapper);

        // 构建分页对象
        Page<EduCourse> page = new Page<>(current, size);
        // 构建查询条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        // 判断多条件组合查询是否为空，不为空才进行查询条件的具体构建
        if (!GuLiUtils.checkObjFieldAllNull(queryWrapper)) {

            // 获得传入的EduTeacher多条件组合查询对象，并转化为map<数据表字段名，多条件组合查询对象属性值>
            Map<String, Object> fields = GuLiUtils.getClassFields(queryWrapper);

            // 遍历map，根据自定义规则（String类型模糊查询，Integer类型匹配查询，两个时间类字段比较查询）对查询条件进行构建
            Set<Map.Entry<String, Object>> entrySet = fields.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.println("<K,V>=" + key + ":" + value);
                // 前端课程界面按更新时间降序排序
                if (value != null && "remake".equals(key) && value.equals("timeSortDesc")){
                    wrapper.orderByDesc("gmt_modified");
                    continue;
                }
                // 前端课程界面按更新时间增序排序
                if (value != null && "remake".equals(key) && value.equals("timeSortAsc")){
                    wrapper.orderByAsc("gmt_modified");
                    continue;
                }
                // 前端课程界面按价格降序排序
                if (value != null && "remake".equals(key) && value.equals("priceSortDesc")){
                    wrapper.orderByDesc("price");
                    continue;
                }
                // 前端课程界面按价格增序排序
                if (value != null && "remake".equals(key) && value.equals("priceSortAsc")){
                    wrapper.orderByAsc("price");
                    continue;
                }

                if (value != null && (value.getClass() == String.class) && !"remake".equals(key)) {
                    wrapper.like(key, value);
                }
                if (value != null && (value.getClass() == Integer.class)) {
                    wrapper.eq(key, value);
                }
                // 时间类条件则分拆后进行between条件构建，由于前端采用的组件确定传值为一个两元素数组，故程序写死进行判断
                if (value != null &&  !"remake".equals(key) && ("gmt_create".equals(key) || "gmt_modified".equals(key))) {
                    ArrayList<String> dates = (ArrayList<String>) value;
                    Date begin = GuLiUtils.parseDate(dates.get(0));
                    Date end = GuLiUtils.parseDate(dates.get(1));
                    wrapper.between(key, begin, end);
                }

            }

        }
        // 利用分页对象和查询条件查出所需要的分页信息
        IPage<EduCourse> resultPage = eduCourseService.page(page, wrapper);

        return Result.ok().data("resultPage", resultPage);
    }

    @ApiOperation(value = "根据课程id删除课程，并同步删除所有课程包含的阿里云vod视频")
    @DeleteMapping("deleteCourse/{id}")
    public Result deleteCourse(@PathVariable("id") String courseId) {

        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
        // 根据课程id获取所有小节的vod视频id
        List<String> videoSourceIdList = new ArrayList<>();
        String videoSourceIds = "";
        List<EduVideo> videoList = eduVideoService.list(videoWrapper);

        for (EduVideo video : videoList) {
            videoSourceIdList.add(video.getVideoSourceId());
            videoSourceIds = CollUtil.join(videoSourceIdList, ",");
        }

        // 远程调用9001批量删除vod视频
        if (videoSourceIds.length() > 0) {
            boolean vodResult = aliyunVodFeignService.deleteVod(videoSourceIds);
            if (!vodResult) {
                throw new RuntimeException("删除阿里云vod视频失败");
            }
        }

        // 删除小节
        boolean videoResult = eduVideoService.remove(videoWrapper);
        if (!videoResult) {
            throw new RuntimeException("删除小节（数据库）失败");
        }

        // 删除章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId);
        boolean chapterResult = eduChapterService.remove(chapterWrapper);
        if (!chapterResult) {
            throw new RuntimeException("删除章节（数据库）失败");
        }

        // 删除课程
        boolean courseResult = eduCourseService.removeById(courseId);
        if (courseResult) {
            return Result.ok();
        } else {
            return Result.error().message("删除课程（数据库）失败");
        }

    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id点赞")
    @PostMapping("addCourseLike/{courseId}")
    public boolean addCourseLike(@PathVariable("courseId") String courseId){
        EduCourse eduCourse = eduCourseService.getById(courseId);
        if(eduCourse == null){
            return false;
        }
        eduCourse.setLikeCount(eduCourse.getLikeCount() + 1);
        return eduCourseService.updateById(eduCourse);
    }

    // 内部接口，返回值不为Result
    @ApiOperation(value = "根据课程id取消点赞")
    @DeleteMapping("deleteCourseLike/{courseId}")
    public boolean deleteCourseLike(@PathVariable("courseId") String courseId){
        EduCourse eduCourse = eduCourseService.getById(courseId);
        if(eduCourse == null){
            return false;
        }
        eduCourse.setLikeCount(eduCourse.getLikeCount() - 1);
        return eduCourseService.updateById(eduCourse);
    }
}

