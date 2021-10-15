package com.Surnia.vodService.controller;

import com.Surnia.commons.exception.MyException;
import com.Surnia.commons.utils.Result;
import com.Surnia.vodService.init.InitVod;
import com.Surnia.vodService.service.VodService;
import com.Surnia.vodService.utils.VodUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName VodController
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/14
 * @Version 1.0
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "视频点播管理")
@RestController
@RequestMapping("/vodService/vod")
public class VodController {
    @Resource
    private VodService vodService;

    // http://localhost:9001/vodService/vod/uploadVod
    @ApiOperation(value = "上传视频到阿里云视频点播")
    @PostMapping("uploadVod")
    public Result uploadVod(MultipartFile file){
        String videoId = vodService.uploadVideo(file);
        return Result.ok().data("videoId", videoId);
    }





    // https://help.aliyun.com/document_detail/61064.html
    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("getVideoPlayAuth")
    public Result getVideoPlayAuth(String vid){
        DefaultAcsClient client = InitVod.initVodClient(VodUtils.VOD_ACCESS_KEY_ID, VodUtils.VOD_ACCESS_KEY_SECRET);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(vid);
            response = client.getAcsResponse(request);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
            System.out.print("RequestId = " + response.getRequestId() + "\n");
            return Result.ok().data("playAuth", response.getPlayAuth());
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new MyException(444,e.getLocalizedMessage());
        }
    }



    // 内部调用端口，不直接提供给前端调用，故返回值不是Result类型
    @ApiOperation(value = "根据VodId从阿里云视频点播删除视频")
    @DeleteMapping("deleteVod/{videoSourceId}")
    public boolean deleteVod(@PathVariable("videoSourceId") String videoSourceId){
        return vodService.deleteVodById(videoSourceId);
    }

    // 内部调用端口，不直接提供给前端调用，故返回值不是Result类型
    @ApiOperation(value = "根据VodId从阿里云视频点播批量删除视频")
    @DeleteMapping("deleteVodByBatch")
    public boolean deleteVodByBatch(List<String> videoSourceIds){
        return vodService.deleteVodByBatch(videoSourceIds);
    }
}
