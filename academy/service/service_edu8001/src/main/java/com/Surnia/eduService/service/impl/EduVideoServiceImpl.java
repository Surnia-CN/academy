package com.Surnia.eduService.service.impl;

import com.Surnia.eduService.entity.EduChapter;
import com.Surnia.eduService.entity.EduVideo;
import com.Surnia.eduService.mapper.EduChapterMapper;
import com.Surnia.eduService.mapper.EduVideoMapper;
import com.Surnia.eduService.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-08
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Resource
    private EduVideoMapper eduVideoMapper;

    @Override
    public List<EduVideo> getVideoByCourseId(String courseId) {
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id", courseId);
        eduVideoQueryWrapper.orderByAsc("sort");
        eduVideoQueryWrapper.orderByAsc("gmt_modified");
        return eduVideoMapper.selectList(eduVideoQueryWrapper);
    }
}
