package com.Surnia.eduService.mapper;

import com.Surnia.eduService.entity.EduSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-06-30
 */
public interface EduSubjectMapper extends BaseMapper<EduSubject> {

    void saveByEasyExcel(List<EduSubject> list);
}
