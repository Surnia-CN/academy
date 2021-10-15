package com.Surnia.ossService.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.Surnia.ossService.service.OssService;
import com.Surnia.ossService.utils.OssUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @ClassName OssServiceImpl
 * @Description TODO
 * @Author Surnia
 * @Date 2021/6/29
 * @Version 1.0
 */

@Service
public class OssServiceImpl implements OssService {


    @Override
    public String uploadAvatar(MultipartFile file) {
        String fileURL;

        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = OssUtils.OSS_END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = OssUtils.OSS_ACCESS_KEY_ID;
        String accessKeySecret = OssUtils.OSS_ACCESS_KEY_SECRET;
        String bucketName = OssUtils.OSS_BUCKET_NAME;

        fileURL = "https://" + bucketName + "." + endpoint + "/";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 得到上传文件流
            InputStream inputStream = file.getInputStream();

            // 定义文件名
            // 如果原始文件名中存在中文，则上传的文件名会出现中文，但是fileURL中则是utf-8的%xx表示。业务逻辑与实际情况不符合。
            //String fileName = "0321/" + file.getOriginalFilename();

            // 调用hutool工具，生成当前日期信息，以及uuid信息
            DateTime date = DateUtil.date();
            String year = DateUtil.format(date, "yyyy");
            String month = DateUtil.format(date, "MM");
            String day = DateUtil.format(date, "dd");
            String simpleUUID = IdUtil.simpleUUID();

            // 定义文件名，规则是按日期进行文件夹分类，文件名为uuid，避免中文。
            // 希望实现按用户id进行文件夹分类，todo
            String fileName = year + "/" + month + "/" + day + "/" + simpleUUID + "file.jpg";

            fileURL += fileName;

            // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, fileName, inputStream);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

        return fileURL;
    }
}
