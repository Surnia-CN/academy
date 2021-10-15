package com.Surnia.wechatLoginService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName WechatLoginMain8160
 * @Description TODO
 * @Author Surnia
 * @Date 2021/8/15
 * @Version 1.0
 */

@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.Surnia")
// 父工程service中添加了mybatis的依赖，所以项目启动时会启动DataSourceAutoConfiguration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WechatLoginMain8160 {
    public static void main(String[] args) {
        SpringApplication.run(WechatLoginMain8160.class,args);
    }
}
