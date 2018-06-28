package com.lance.chart.runner;

import com.lance.chart.util.SaveFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 画图的具体 task, 会根据 startDate 规定的日期，读取 3 分钟以内的表数据作图
 */
@Slf4j
@Component
public class SaveFileTask {

    @Autowired
    private DrawChart drawChart;

    @Autowired
    private SaveFileUtil saveFileUtil;

    /**
     * 保存以 endDate 为截止时间的前 3 分钟的图到本地
     * @param endDate
     * @throws Exception
     */
    public void saveChartToImageTask(Date endDate) throws Exception {

        //生成 chart-demo.png
        long time = System.currentTimeMillis();
        String fileName = "chart-demo-"+ time;

        String optionJsonString = drawChart.draw(endDate, 3);
        List<String> list = saveFileUtil.saveHtmlEchartToLocalImg(optionJsonString, fileName);
        System.out.println(list);
    }

}