package com.Surnia.cmsService.controller;

import com.Surnia.cmsService.feign.EduFeignService;
import com.Surnia.cmsService.feign.VodFeignService;
import com.Surnia.commons.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName CmsVodController
 * @Description TODO
 * @Author Surnia
 * @Date 2021/9/6
 * @Version 1.0
 */
@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "前台系统视频点播管理")
@RestController
@RequestMapping("/cmsService/vod")
public class CmsVodController {
    @Resource
    private VodFeignService vodFeignService;
    @Resource
    private EduFeignService eduFeignService;

    @ApiOperation(value = "根据课程id获取视频id")
    @GetMapping("getVidByVideoId/{videoId}")
    public Result getVidByVideoId(@PathVariable("videoId") String videoId){
        System.out.println("quary......");
        return eduFeignService.getVidByVideoId(videoId);
    }

    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("getVideoPlayAuth/{vid}")
    public Result getVideoPlayAuth(@PathVariable("vid")String vid){
        return vodFeignService.getVideoPlayAuth(vid);
    }
}
