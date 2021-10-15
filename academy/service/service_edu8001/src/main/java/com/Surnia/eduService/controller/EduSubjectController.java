package com.Surnia.eduService.controller;


import com.Surnia.commons.utils.Result;
import com.Surnia.eduService.entity.EduSubject;
import com.Surnia.eduService.entity.vo.EduSubjectChildren;
import com.Surnia.eduService.entity.vo.EduSubjectList;
import com.Surnia.eduService.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-06-30
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "课程分类管理")
@RestController
@RequestMapping("/eduService/subject")
public class EduSubjectController {

    @Resource
    private EduSubjectService eduSubjectService;

    @ApiOperation(value = "查询所有课程分类")
    @GetMapping("getAllSubject")
    public Result getAllSubject(){
        // 从数据库中查询出所有的课程分类
        List<EduSubject> list = eduSubjectService.getAllSubject();

        // 创建最终返回的课程分类对象
        List<EduSubjectList> resultList = new ArrayList<>();
        // 创建定位符i
        int i = 0;

        // 遍历数据库中的每个对象，将其按要求封装到最终返回对象里
        for (EduSubject eduSubject : list) {
            if (eduSubject.getParentId().equals("0")) {
                // 如果对象的parentId为0，则将其复制封装到一个EduSubjectList对象中，并将此对象加入resultList
                EduSubjectList subjectList = new EduSubjectList();
                BeanUtils.copyProperties(eduSubject, subjectList);
                resultList.add(subjectList);
                i++;
            } else {
                // 如果对象的parentId不为0，根据业务规则，其为上一个subjectList的childrenList属性中的一个元素
                // 故将其复制封装到一个EduSubjectChildren对象中，并将此对象加入index为i-1的subjectList的childrenList中
                EduSubjectChildren children = new EduSubjectChildren();
                BeanUtils.copyProperties(eduSubject,children);
                resultList.get(i - 1).getChildrenList().add(children);
            }
        }
        return Result.ok().data("subjectList", resultList);
    }

    @ApiOperation(value = "根据课程分类id查询课程分类名")
    @GetMapping("getSubjectNameById/{subjectId}")
    public Result getSubjectNameById(@PathVariable("subjectId")String subjectId){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("id", subjectId);
        EduSubject subject = eduSubjectService.getOne(wrapper);
        return Result.ok().data("subjectName", subject.getTitle());
    }

    @ApiOperation(value = "根据Excel表将数据存入数据库")
    @PostMapping("uploadFileByExcel")
    public Result addSubject(MultipartFile file){
        try {
            eduSubjectService.saveFileByExcel(file);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error().data("exception", e.getMessage());
        }
    }

}

