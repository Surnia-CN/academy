package com.Surnia.vodService.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {

    String uploadVideo(MultipartFile file);

    boolean deleteVodById(String videoSourceId);

    boolean deleteVodByBatch(List<String> videoSourceIds);
}
