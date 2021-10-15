package com.Surnia.eduService.service.impl;

import com.Surnia.eduService.entity.EduTeacher;
import com.Surnia.eduService.mapper.EduTeacherMapper;
import com.Surnia.eduService.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-06-20
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {
    @Resource
    private EduTeacherMapper eduTeacherMapper;

    @Override
    public List<EduTeacher> getHot() {
        QueryWrapper<EduTeacher> hotTeacherWrapper = new QueryWrapper<>();
        hotTeacherWrapper.eq("level", 2);
        hotTeacherWrapper.orderByDesc("sort","gmt_modified");
        hotTeacherWrapper.last("limit 4");
        return eduTeacherMapper.selectList(hotTeacherWrapper);
    }
}
