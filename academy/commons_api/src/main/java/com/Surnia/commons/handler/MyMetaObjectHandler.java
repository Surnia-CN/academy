package com.Surnia.commons.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName MyMetaObjectHandler
 * @Description 配置mp的属性自动填充功能处理器
 * @Author Surnia
 * @Date 2021/6/21
 * @Version 1.0
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        log.info("insert gmtCreate fill ....value:" + new Date());
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        log.info("insert gmtModified fill ....value:" + new Date());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        log.info("update gmtModified fill ....value:" + new Date());

    }
}