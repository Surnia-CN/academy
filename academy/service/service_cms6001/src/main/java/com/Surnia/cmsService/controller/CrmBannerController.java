package com.Surnia.cmsService.controller;


import com.Surnia.cmsService.entity.CrmBanner;
import com.Surnia.cmsService.feign.EduFeignService;
import com.Surnia.cmsService.service.CrmBannerService;
import com.Surnia.commons.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-21
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "前台系统首页管理")
@RestController
@RequestMapping("/cmsService/indexCms")
public class CrmBannerController {
    @Resource
    private CrmBannerService crmBannerService;
    @Resource
    private EduFeignService eduFeignService;

    @ApiOperation(value = "查询所有banner")
    @GetMapping("getAllBanner")
    public Result getAllBanner(){
        List<CrmBanner> banners = crmBannerService.list(null);
        return Result.ok().data("banners", banners);
    }

    @ApiOperation(value = "查询热门课程")
    @GetMapping("getHotCourse")
    public Result getHotCourse(){
        return eduFeignService.getHotCourse();
    }

    @ApiOperation(value = "查询名师")
    @GetMapping("getHotTeacher")
    public Result getHotTeacher(){
        return eduFeignService.getHotTeacher();
    }


}

