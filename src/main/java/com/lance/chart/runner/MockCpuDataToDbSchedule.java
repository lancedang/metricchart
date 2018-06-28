package com.lance.chart.runner;

import com.lance.chart.dao.CpuDAO;
import com.lance.chart.entity.CPUEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MockCpuDataToDbSchedule{

    @Autowired
    private CpuDAO cpuDAO;

    //每隔 1 秒插入一条 CPU percent 数据
    @Scheduled(fixedRate = 3000)
    public void flushData() throws Exception {
        CPUEntity cpuEntity = new CPUEntity();
        cpuEntity.setPackageName("com.lance.chart");

        //随机生成 [0, 100) 的 cpu percent
        double cpuPercent = Math.random() * 100;

        cpuEntity.setCpuPercent(cpuPercent);
        cpuEntity.setCreateDate(new Date());

        cpuDAO.save(cpuEntity);
    }
}
