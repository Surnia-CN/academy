package com.Surnia.userService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName ServiceUserMain5001
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/26
 * @Version 1.0
 */

@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.Surnia")
@MapperScan(value = "com.Surnia.userService.mapper")
@SpringBootApplication
public class ServiceUserMain5001 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserMain5001.class,args);
    }
}
