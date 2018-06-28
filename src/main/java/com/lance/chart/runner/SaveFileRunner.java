package com.lance.chart.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.Date;

/**
 * 执行一次保存 file 动作，可通过 SaveFileSchedule 来实现
 */
//@Component
public class SaveFileRunner implements CommandLineRunner {

    @Autowired
    private SaveFileTask runner;

    @Override
    public void run(String... strings) throws Exception {
        Date endDate = new Date();
        //Thread.sleep(1000 * 60 * 2);
        System.out.println("save file thread: " + Thread.currentThread().getName());
        runner.saveChartToImageTask(endDate);
    }
}
