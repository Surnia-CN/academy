package com.Surnia.eduService.listener;

import cn.hutool.json.JSON;
import com.Surnia.commons.handler.MyMetaObjectHandler;
import com.Surnia.eduService.entity.EduSubject;
import com.Surnia.eduService.mapper.EduSubjectMapper;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EasyExcelDataListener
 * @Description TODO
 * @Author Surnia
 * @Date 2021/6/30
 * @Version 1.0
 */
@Slf4j
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class EasyExcelDataListener extends AnalysisEventListener<EduSubject>{
    //private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelDataListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    List<EduSubject> list = new ArrayList<>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */

    private EduSubjectMapper eduSubjectMapper;


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param eduSubjectMapper
     */
    public EasyExcelDataListener(EduSubjectMapper eduSubjectMapper) {
        this.eduSubjectMapper = eduSubjectMapper;
    }
    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(EduSubject data, AnalysisContext context) {
        //LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        log.info("invoke...");
        log.info("data:" + data.toString());
        log.info("context:" + context.toString());

        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
        }
    }
    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        log.info("doAfterAllAnalysed...");
        saveData();
        //LOGGER.info("所有数据解析完成！");
    }
    /**
     * 加上存储数据库
     */
    private void saveData() throws RuntimeException{
        //LOGGER.info("{}条数据，开始存储数据库！", list.size());

        log.info("saveData...");
        log.info("{}条数据，开始存储数据库！", list.size());
        eduSubjectMapper.saveByEasyExcel(list);
        // 存储完成清理 list
        list.clear();
        //LOGGER.info("存储数据库成功！");
    }
}
