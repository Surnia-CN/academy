package com.Surnia.eduService.service;

import com.Surnia.eduService.entity.EduCourse;
import com.Surnia.eduService.entity.vo.EduCourseInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-07-08
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(EduCourseInfo eduCourseInfo);

    EduCourseInfo getCourseInfoById(String courseId);

    String updateCourseInfo(EduCourseInfo eduCourseInfo);

    List<EduCourse> getHot();
}
