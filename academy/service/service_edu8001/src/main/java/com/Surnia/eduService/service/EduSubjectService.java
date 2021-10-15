package com.Surnia.eduService.service;

import com.Surnia.eduService.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-06-30
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveFileByExcel(MultipartFile file);

    List<EduSubject> getAllSubject();
}
