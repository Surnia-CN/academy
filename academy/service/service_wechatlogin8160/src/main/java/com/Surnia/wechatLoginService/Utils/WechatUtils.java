package com.Surnia.wechatLoginService.Utils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName WechatUtils
 * @Description TODO
 * @Author Surnia
 * @Date 2021/8/15
 * @Version 1.0
 */

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatUtils implements InitializingBean {

    // 使用@ConfigurationProperties注解获取到配置文件中的值，需要保证属性名和配置文件中的名字完全一致
    private String appid;
    private String appsecret;
    private String redirecturl;

    // static类型的属性的值无法由配置文件的值注入，故本类实现InitializingBean接口
    // 旨在调用afterPropertiesSet方法，来为以下static属性赋值
    public static String WECHAT_APP_ID;
    public static String WECHAT_APP_SECRET;
    public static String WECHAT_REDIRECT_URL;



    @Override
    public void afterPropertiesSet() throws Exception {
        WECHAT_APP_ID = appid;
        WECHAT_APP_SECRET = appsecret;
        WECHAT_REDIRECT_URL = redirecturl;
    }
}
