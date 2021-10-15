package com.Surnia.ossService;

import com.Surnia.ossService.utils.OssUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName ServiceOssMain7001
 * @Description TODO
 * @Author Surnia
 * @Date 2021/6/29
 * @Version 1.0
 */

@EnableDiscoveryClient
@ComponentScan(basePackages = "com.Surnia")
// 父工程service中添加了mybatis的依赖，所以项目启动时会启动DataSourceAutoConfiguration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ServiceOssMain7001 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOssMain7001.class,args);
    }
}
