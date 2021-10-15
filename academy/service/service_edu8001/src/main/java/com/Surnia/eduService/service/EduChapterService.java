package com.Surnia.eduService.service;

import com.Surnia.eduService.entity.EduChapter;
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
public interface EduChapterService extends IService<EduChapter> {

    List<EduChapter> getChapterByCourseId(String courseId);
}
