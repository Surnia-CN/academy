package com.Surnia.eduService.service;

import com.Surnia.eduService.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-07-08
 */
public interface EduVideoService extends IService<EduVideo> {

    List<EduVideo> getVideoByCourseId(String courseId);
}
