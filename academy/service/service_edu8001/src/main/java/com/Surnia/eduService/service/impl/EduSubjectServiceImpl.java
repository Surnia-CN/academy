package com.Surnia.eduService.service.impl;

import com.Surnia.eduService.entity.EduSubject;
import com.Surnia.eduService.listener.EasyExcelDataListener;
import com.Surnia.eduService.mapper.EduSubjectMapper;
import com.Surnia.eduService.service.EduSubjectService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-06-30
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    @Resource
    EduSubjectMapper eduSubjectMapper;

    @Override
    public void saveFileByExcel(MultipartFile file) {


        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：

        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        try {
            EasyExcel.read(file.getInputStream(), EduSubject.class, new EasyExcelDataListener(eduSubjectMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<EduSubject> getAllSubject() {
        return eduSubjectMapper.selectList(null);
    }
}
