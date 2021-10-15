package com.Surnia.eduService.service.impl;

import com.Surnia.eduService.entity.EduCourse;
import com.Surnia.eduService.entity.EduCourseDescription;
import com.Surnia.eduService.entity.EduTeacher;
import com.Surnia.eduService.entity.vo.EduCourseInfo;
import com.Surnia.eduService.mapper.EduCourseDescriptionMapper;
import com.Surnia.eduService.mapper.EduCourseMapper;
import com.Surnia.eduService.mapper.EduTeacherMapper;
import com.Surnia.eduService.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-08
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Resource
    private EduCourseMapper eduCourseMapper;
    @Resource
    private EduCourseDescriptionMapper eduCourseDescriptionMapper;
    @Resource
    private EduTeacherMapper eduTeacherMapper;

    @Override
    public String saveCourseInfo(EduCourseInfo eduCourseInfo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(eduCourseInfo, eduCourse);
        int result = eduCourseMapper.insert(eduCourse);
        if (result == 0){
            throw new RuntimeException("添加课程信息失败");
        }

        EduCourseDescription eduCourseDescription = new EduCourseDescription();

        eduCourseDescription.setDescription(eduCourseInfo.getDescription());
        // 简介id与课程id绑定
        eduCourseDescription.setId(eduCourse.getId());

        result = eduCourseDescriptionMapper.insert(eduCourseDescription);
        if (result == 0){
            throw new RuntimeException("添加课程简介信息失败");
        }
        return eduCourse.getId();
    }

    @Override
    public String updateCourseInfo(EduCourseInfo eduCourseInfo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(eduCourseInfo, eduCourse);
        int resultA = eduCourseMapper.updateById(eduCourse);
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(eduCourseInfo, eduCourseDescription);
        int resultB = eduCourseDescriptionMapper.updateById(eduCourseDescription);
        return eduCourse.getId();
    }

    @Override
    public List<EduCourse> getHot() {
        QueryWrapper<EduCourse> hotCourseWrapper = new QueryWrapper<>();
        hotCourseWrapper.orderByDesc("view_count","gmt_modified");
        hotCourseWrapper.last("limit 8");
        return eduCourseMapper.selectList(hotCourseWrapper);
    }

    @Override
    public EduCourseInfo getCourseInfoById(String courseId) {
        EduCourse eduCourse = eduCourseMapper.selectById(courseId);
        EduCourseDescription eduCourseDescription = eduCourseDescriptionMapper.selectById(courseId);
        EduTeacher eduTeacher = eduTeacherMapper.selectById(eduCourse.getTeacherId());
        if(eduTeacher == null || eduCourseDescription == null){
            throw new RuntimeException("查询的课程简介或讲师信息不完整，课程id为：[ "+courseId+" ]，请核对。");
        }
        EduCourseInfo eduCourseInfo = new EduCourseInfo();
        BeanUtils.copyProperties(eduCourse, eduCourseInfo);
        eduCourseInfo.setDescription(eduCourseDescription.getDescription());
        eduCourseInfo.setTeacherName(eduTeacher.getName());

        return eduCourseInfo;
    }


}
