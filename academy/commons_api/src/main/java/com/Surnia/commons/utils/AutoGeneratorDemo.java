package com.Surnia.commons.utils;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

/**
 * @ClassName AutoGeneratorDemo
 * @Description 利用Mybatis-Plus代码生成器生成对应数据库表的代码，路径8001test文件夹下
 * @Author Surnia
 * @Date 2021/6/30
 * @Version 1.0
 */

public class AutoGeneratorDemo {
    @Test
    public void test(){
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir("F:\\program files\\idea-workspace\\guli\\service\\service_edu8001" + "/src/main/java");// 绝对路径设置代码生成的位置
        gc.setAuthor("testjava");
        gc.setOpen(false);
        gc.setFileOverride(false);
        gc.setServiceName("%sService");// 去除自动生成的类名中开头的“I”
        gc.setIdType(IdType.ID_WORKER);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true); //实体属性 Swagger2 注解

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();

        dsc.setUrl("jdbc:mysql://localhost:3306/guli?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("999333");
        dsc.setDbType(DbType.MYSQL);

        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.Surnia");// 指定包名
        pc.setModuleName("eduService");// 指定模块名

        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");

        mpg.setPackageInfo(pc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();

        strategy.setInclude("edu_chapter","edu_course","edu_course_description","edu_video");//指定数据库表名

        strategy.setNaming(NamingStrategy.underline_to_camel);// 设置数据库名转换类名的风格
        strategy.setTablePrefix(pc.getModuleName() + "_");

        strategy.setColumnNaming(NamingStrategy.underline_to_camel);// 设置数据库字段名转换属性名的风格
        strategy.setEntityLombokModel(true);// 【实体】为lombok模型
        strategy.setRestControllerStyle(true);// 生成 @RestController 控制器
        strategy.setControllerMappingHyphenStyle(true);// 驼峰转连字符

        mpg.setStrategy(strategy);


        mpg.execute();
    }
}
