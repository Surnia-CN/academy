package com.Surnia.vodService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName ServiceVodMain9001
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/14
 * @Version 1.0
 */

@EnableDiscoveryClient
@ComponentScan(basePackages = "com.Surnia")
// 父工程service中添加了mybatis的依赖，所以项目启动时会启动DataSourceAutoConfiguration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ServiceVodMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVodMain9001.class,args);
    }
}
