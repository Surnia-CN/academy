package com.Surnia.vodService.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.Surnia.vodService.init.InitVod;
import com.Surnia.vodService.service.VodService;
import com.Surnia.vodService.utils.VodUtils;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.Surnia.vodService.init.InitVod.initVodClient;

/**
 * @ClassName VodServiceImpl
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/14
 * @Version 1.0
 */

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideo(MultipartFile file) {

        String accessKeyId = VodUtils.VOD_ACCESS_KEY_ID;
        String accessKeySecret = VodUtils.VOD_ACCESS_KEY_SECRET;
        String fileName = file.getOriginalFilename();

        // 调用hutool工具，生成当前日期信息，以及uuid信息
        DateTime date = DateUtil.date();
        String year = DateUtil.format(date, "yyyy");
        String month = DateUtil.format(date, "MM");
        String day = DateUtil.format(date, "dd");
        String simpleUUID = IdUtil.simpleUUID();

        // 定义文件名，规则是按日期进行文件夹分类，文件名为uuid，避免中文。
        // 希望实现按用户id进行文件夹分类，todo
        String title = year + "/" + month + "/" + day + "/" + simpleUUID;

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }

        return response.getVideoId();
    }

    @Override
    public boolean deleteVodById(String videoSourceId) {
        String accessKeyId = VodUtils.VOD_ACCESS_KEY_ID;
        String accessKeySecret = VodUtils.VOD_ACCESS_KEY_SECRET;
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);

        DeleteVideoResponse response = new DeleteVideoResponse();

        try {
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoSourceId);
            response = client.getAcsResponse(request);
            System.out.print("RequestId = " + response.getRequestId() + "\n");
            return true;

        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public boolean deleteVodByBatch(List<String> videoSourceIds) {
        String accessKeyId = VodUtils.VOD_ACCESS_KEY_ID;
        String accessKeySecret = VodUtils.VOD_ACCESS_KEY_SECRET;
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);

        DeleteVideoResponse response = new DeleteVideoResponse();

        try {
            DeleteVideoRequest request = new DeleteVideoRequest();

            String deleteIds = CollUtil.join(videoSourceIds, ",");
            System.out.println(deleteIds);
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(deleteIds);
            response = client.getAcsResponse(request);
            System.out.print("RequestId = " + response.getRequestId() + "\n");
            return true;

        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            return false;
        }
    }
}
