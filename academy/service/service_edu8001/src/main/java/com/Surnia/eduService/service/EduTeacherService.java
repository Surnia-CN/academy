package com.Surnia.eduService.service;

import com.Surnia.eduService.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-06-20
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> getHot();
}
