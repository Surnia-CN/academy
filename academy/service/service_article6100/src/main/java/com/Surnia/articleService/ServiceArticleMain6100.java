package com.Surnia.articleService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName ServiceArticleMain6100
 * @Description TODO
 * @Author Surnia
 * @Date 2021/9/14
 * @Version 1.0
 */

@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.Surnia")
@MapperScan(value = "com.Surnia.articleService.mapper")
@SpringBootApplication
public class ServiceArticleMain6100 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceArticleMain6100.class, args);
    }
}
