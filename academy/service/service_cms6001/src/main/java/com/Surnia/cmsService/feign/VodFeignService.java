package com.Surnia.cmsService.feign;

import com.Surnia.commons.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-aliyunvod")
public interface VodFeignService {
    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("/vodService/vod/getVideoPlayAuth")
    public Result getVideoPlayAuth(@RequestParam("vid")String vid);
}
