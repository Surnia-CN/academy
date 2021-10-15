package com.Surnia.commons.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GuLiUtils
 * @Description 编写项目通用的工具类
 * @Author Surnia
 * @Date 2021/6/21
 * @Version 1.0
 */


public class GuLiUtils {


    /**
     * @param object： 多条件查询类的对象，要求是该类的属性名与数据表中字段名完全一致（满足Java属性名小驼峰命名与mysql下划线命名转换。）
     * @return Map <String,Object>： String为数据表对应的字段名，Object为对象的属性值
     * @description: 处理多条件组合查询对象，通过对象的public getXXX方法名，获取到与之对应的数据表字段名。将结果存入map中。
     * @author: Surnia
     * @date: 2021/6/21
     */
    public static Map<String, Object> getClassFields(Object object) {
        Map<String, Object> map = new HashMap<>();
        try {
            Method[] methods = object.getClass().getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                // 判断是否get开头方法，并去除getClass方法
                if (methodName.startsWith("get") && !"getClass".equals(methodName)) {
                    Object fieldValue = method.invoke(object, null);
                    // 通过方法名还原数据表字段名
                    map.put(getFieldNameByMethodName(methodName), fieldValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 还原属性名。即去除方法名的get，将首字母大写改为小写。满足Java属性小驼峰命名与mysql下划线命名转换。
    private static String getFieldNameByMethodName(String methodName) {
        // 去除“get”
        String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        // 判断小驼峰命名，并转换为mysql的下划线命名
        for (int i = 0; i < fieldName.length(); i++) {
            if (fieldName.charAt(i) >= 65 && fieldName.charAt(i) <= 90) {
                // 大写字母转换为小写字母，并前缀增加下划线
                char temp = fieldName.charAt(i);
                temp += 32;
                String newStr = "_" + temp;
                fieldName = fieldName.replace(String.valueOf(fieldName.charAt(i)), newStr);
            }
        }
        return fieldName;
    }


    /**
     * @param dateTime：国际标准时间格式的String("2021-06-21T12:00:00.000Z")
     * @return Date：北京时间的Date数据(Mon Jun 21 20:00:00 CST 2021)
     * @description: 将一个国际标准时间格式的String(" 2021 - 06 - 21T12 : 00 : 00.000Z ")，转换为北京时间的Date数据(Mon Jun 21 20:00:00 CST 2021)
     * @author: Surnia
     * @date: 2021/6/27
     */
    public static Date parseDate(String dateTime) {
        if (dateTime != null) {
            dateTime = dateTime.replace("Z", " UTC");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date resultTime = null;
            try {
                resultTime = format.parse(dateTime);
                //System.out.println(resultTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultTime;
        } else {
            throw new RuntimeException("待转换的dateTime为空");
        }
    }

    /**
     * @description: 判断一个对象的所有属性是否都为空
     * @param obj：	需要判断的对象
     * @return boolean： 属性值全为null返回true，有任一属性不为空返回false
     * @author: Surnia
     * @date: 2021/6/27
     */
    public static boolean checkObjFieldAllNull(Object obj){
        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) != null) {
                    return false;
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return true;
    }

    @Test
    public void test() {
        String a = "getGmtModified";
        String fieldName = getFieldNameByMethodName(a);
        System.out.println(fieldName);
    }
}
