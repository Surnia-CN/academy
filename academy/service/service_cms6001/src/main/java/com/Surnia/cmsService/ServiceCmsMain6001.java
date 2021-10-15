package com.Surnia.cmsService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName ServiceCmsMain6001
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/21
 * @Version 1.0
 */

@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.Surnia")
@MapperScan(value = "com.Surnia.cmsService.mapper")
@SpringBootApplication
public class ServiceCmsMain6001 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmsMain6001.class, args);
    }
}
