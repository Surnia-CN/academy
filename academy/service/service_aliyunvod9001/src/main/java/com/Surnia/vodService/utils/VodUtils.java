package com.Surnia.vodService.utils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName VodUtils
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/14
 * @Version 1.0
 */

@Data
@ConfigurationProperties(prefix = "aliyunvod.client")
@Component
public class VodUtils implements InitializingBean {

    // 使用@ConfigurationProperties注解获取到配置文件中的值，需要保证属性名和配置文件中的名字完全一致
    private String accessKeyId;
    private String accessKeySecret;

    // static类型的属性的值无法由配置文件的值注入，故本类实现InitializingBean接口
    // 旨在调用afterPropertiesSet方法，来为以下static属性赋值
    public static String VOD_ACCESS_KEY_ID;
    public static String VOD_ACCESS_KEY_SECRET;



    @Override
    public void afterPropertiesSet() throws Exception {
        VOD_ACCESS_KEY_ID = accessKeyId;
        VOD_ACCESS_KEY_SECRET = accessKeySecret;
    }
}