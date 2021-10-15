package com.Surnia.eduService.controller;


import com.Surnia.commons.utils.GuLiUtils;
import com.Surnia.commons.utils.Result;
import com.Surnia.eduService.entity.EduTeacher;
import com.Surnia.eduService.entity.vo.EduTeacherQueryWrapper;
import com.Surnia.eduService.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-06-20
 */
@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/eduService/teacher")
public class EduTeacherController {

    @Resource
    private EduTeacherService eduTeacherService;

    // 根据id查询讲师
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("get/{id}")
    public Result getTeacher(@PathVariable("id") long id) {
        EduTeacher teacher = eduTeacherService.getById(id);
        return Result.ok().data("TeacherInfo", teacher);
    }

    // 查询所有讲师
    @ApiOperation(value = "查询所有讲师")
    @GetMapping("getAll")
    public Result getAll() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return Result.ok().data("TeacherList", list);
    }

    // 以首席讲师和排序为依据
    @ApiOperation(value = "查询名师(4位)")
    @GetMapping("getHotTeacher")
    public  Result getHotTeacher(){
        List<EduTeacher> list = eduTeacherService.getHot();
        return Result.ok().data("hotTeacherList", list);
    }

    // 分页查询所有讲师
    @ApiOperation(value = "分页查询所有讲师")
    @GetMapping("getAllByPage/{page}/{size}")
    public Result getAllByPage(@PathVariable("page") long current, @PathVariable("size") long size) {
        Page<EduTeacher> page = new Page<>(current, size);
        IPage<EduTeacher> resultPage = eduTeacherService.page(page, null);
        return Result.ok().data("resultPage", resultPage);
    }

    // 多条件组合分页查询所有讲师
    @ApiOperation(value = "多条件组合分页查询所有讲师")
    @PostMapping("getPageByWrapper/{page}/{size}")
    public Result getPageByWrapper(@PathVariable("page") long current
            , @PathVariable("size") long size
            , @RequestBody(required = false) EduTeacherQueryWrapper queryWrapper) {
        // 构建分页对象
        Page<EduTeacher> page = new Page<>(current, size);
        // 构建查询条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        // 判断多条件组合查询是否为空，不为空才进行查询条件的构建
        if (!GuLiUtils.checkObjFieldAllNull(queryWrapper)) {
            System.out.println(queryWrapper);
            // 获得传入的EduTeacher多条件组合查询对象，并转化为map<数据表字段名，多条件组合查询对象属性值>
            Map<String, Object> fields = GuLiUtils.getClassFields(queryWrapper);

            // 遍历map，根据自定义规则（String类型模糊查询，Integer类型匹配查询，两个时间类字段比较查询）对查询条件进行构建
            Set<Map.Entry<String, Object>> entrySet = fields.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.println("<K,V>=" + key + ":" + value);
                if (value != null && (value.getClass() == String.class)) {
                    wrapper.like(key, value);
                }
                if (value != null && (value.getClass() == Integer.class)) {
                    wrapper.eq(key, value);
                }
                // 时间类条件则分拆后进行between条件构建，由于前端采用的组件确定传值为一个两元素数组，故程序写死进行判断
                if (value != null && ("gmt_create".equals(key) || "gmt_modified".equals(key))) {
                    ArrayList<String> dates = (ArrayList<String>) value;
                    Date begin = GuLiUtils.parseDate(dates.get(0));
                    Date end = GuLiUtils.parseDate(dates.get(1));
                    wrapper.between(key, begin, end);
                }
            }
        }
        // 利用分页对象和查询条件查出所需要的分页信息
        IPage<EduTeacher> resultPage = eduTeacherService.page(page, wrapper);
        return Result.ok().data("resultPage", resultPage);
    }

    // 新增讲师
    @ApiOperation(value = "新增讲师")
    @PostMapping("create")
    public Result addTeacher(@RequestBody EduTeacher teacher) {
        boolean result = eduTeacherService.save(teacher);
        return result ? Result.ok().data("NewTeacherInfo", teacher) : Result.error();
    }

    // 根据id删除讲师
    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("delete/{id}")
    public Result removeTeacher(@PathVariable("id") String id) {
        boolean result = eduTeacherService.removeById(id);
        return result ? Result.ok() : Result.error();
    }

    // 根据id修改讲师信息
    @ApiOperation(value = "根据id修改讲师信息")
    @PostMapping("update")
    public Result updateTeacher(@RequestBody EduTeacher teacher) {
        boolean result = eduTeacherService.updateById(teacher);
        return result ? Result.ok().data("TeacherNewInfo", teacher) : Result.error();
    }

}

