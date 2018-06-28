package com.lance.chart.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * 定时(每隔30s)自动保存图片
 */
//@Component
@Slf4j
public class SaveFileSchedule {

    @Autowired
    private SaveFileTask runner;

    //30s 打印一次图片
    //@Scheduled(fixedRate = 1000 * 30)
    @Scheduled(initialDelay = 1000 * 60, fixedRate = 1000 * 60 * 5)
    public void saveImageSchedule() throws Exception {
        Date endDate = new Date();
        log.info("save img job startDate: " + endDate);
        runner.saveChartToImageTask(endDate);
    }

}
