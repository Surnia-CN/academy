package com.Surnia.eduService.feign;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(value = "service-aliyunvod")
public interface AliyunVodFeignService {

    // 内部调用端口，不直接提供给前端调用，故返回值不是Result类型
    @ApiOperation(value = "根据id从阿里云视频点播删除视频")
    @DeleteMapping("/vodService/vod/deleteVod/{videoSourceId}")
    public boolean deleteVod(@PathVariable("videoSourceId") String videoSourceId);

    // 内部调用端口，不直接提供给前端调用，故返回值不是Result类型
    @ApiOperation(value = "根据VodId从阿里云视频点播批量删除视频")
    @DeleteMapping("/vodService/vod/deleteVodByBatch")
    public boolean deleteVodByBatch(List<String> videoSourceIds);
}
