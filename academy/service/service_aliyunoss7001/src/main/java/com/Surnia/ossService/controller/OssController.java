package com.Surnia.ossService.controller;

import com.Surnia.commons.utils.Result;
import com.Surnia.ossService.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @ClassName OssController
 * @Description TODO
 * @Author Surnia
 * @Date 2021/6/29
 * @Version 1.0
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "云存储管理")
@RestController
@RequestMapping("/ossService/oss")
public class OssController {

    @Resource
    private OssService ossService;

    @ApiOperation(value = "上传头像到阿里云oss")
    @PostMapping("upload")
    public Result uploadOssFile(MultipartFile file){
        System.out.println(file);
        String url = ossService.uploadAvatar(file);

        return Result.ok().data("url",url);
    }
}
