package com.Surnia.eduService.service.impl;

import com.Surnia.eduService.entity.EduChapter;
import com.Surnia.eduService.mapper.EduChapterMapper;
import com.Surnia.eduService.service.EduChapterService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Resource
    private EduChapterMapper eduChapterMapper;

    @Override
    public List<EduChapter> getChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", courseId);
        eduChapterQueryWrapper.orderByAsc("sort");
        eduChapterQueryWrapper.orderByAsc("gmt_modified");
        return eduChapterMapper.selectList(eduChapterQueryWrapper);
    }
}
