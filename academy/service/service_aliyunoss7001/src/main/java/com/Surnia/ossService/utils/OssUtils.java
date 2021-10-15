package com.Surnia.ossService.utils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName OssUtils
 * @Description TODO
 * @Author Surnia
 * @Date 2021/6/29
 * @Version 1.0
 */

@Data
@ConfigurationProperties(prefix = "aliyunoss.client")
@Component
public class OssUtils implements InitializingBean {

    // 使用@ConfigurationProperties注解获取到配置文件中的值，需要保证属性名和配置文件中的名字完全一致
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    // static类型的属性的值无法由配置文件的值注入，故本类实现InitializingBean接口
    // 旨在调用afterPropertiesSet方法，来为以下static属性赋值
    public static String OSS_END_POINT;
    public static String OSS_ACCESS_KEY_ID;
    public static String OSS_ACCESS_KEY_SECRET;
    public static String OSS_BUCKET_NAME;


    @Override
    public void afterPropertiesSet() throws Exception {
        OSS_END_POINT = endpoint;
        OSS_ACCESS_KEY_ID = accessKeyId;
        OSS_ACCESS_KEY_SECRET = accessKeySecret;
        OSS_BUCKET_NAME = bucketName;
    }
}

