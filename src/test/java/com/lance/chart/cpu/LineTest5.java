package com.lance.chart.cpu;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Line;
//import com.github.abel533.echarts.util.EnhancedOption;
import com.lance.chart.dao.CpuDAO;
import com.lance.chart.entity.CPUEntity;
import com.lance.chart.util.EnhancedOption;
import com.lance.chart.util.ReportUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * @author liuzh
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LineTest5 {

    @Autowired
    private CpuDAO cpuDAO;

    @Test
    public void test() throws ParseException {
        //地址:http://echarts.baidu.com/doc/example/line5.html
        EnhancedOption option = new EnhancedOption();
        option.legend("cpu 使用率与时间的变化关系");

        option.toolbox().show(true).feature(
                Tool.mark,
                Tool.dataView,
                new MagicType(Magic.line, Magic.bar),
                Tool.restore,
                Tool.saveAsImage);

        option.calculable(true);
        option.tooltip().trigger(Trigger.axis).formatter("CPU Usage <br/>{b} : {c}%");


        //设置 y 轴 值轴
        ValueAxis valueAxis = new ValueAxis();
        //valueAxis.type(AxisType.time);
        valueAxis.axisLabel().formatter("{value} %");
        valueAxis.setName("cpu 使用率");
        //option.xAxis(valueAxis);
        option.yAxis(valueAxis);

        //设置 x 轴为时间格式
        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.axisLine().onZero(false);
        categoryAxis.axisLabel().formatter("{value}");
        categoryAxis.boundaryGap(false);
        categoryAxis.setName("时间");

        //从数据库查询 某个时间段的 cpu 使用率
        String start = "2018-05-30 12:42:32.000";
        String end = "2018-05-30 12:43:32.000";
        List<CPUEntity> cpuEntities = cpuDAO.findCPUEntityByDate(ReportUtil.toDate(start), ReportUtil.toDate(end));

        DateFormat df = DateFormat.getTimeInstance();//x 轴只显示时分秒

        //x 轴数据
        Object[] dateList = cpuEntities.stream().map(item -> df.format(item.getCreateDate())).toArray();

        //y 轴数据
        Object[] cpuUsageList = cpuEntities.stream().map(item -> item.getCpuPercent()).toArray();

        //x 轴设置
        categoryAxis.data(dateList);
        option.xAxis(categoryAxis);

        Line line = new Line();

        line.smooth(true).name("cpu 使用率与时间的变化关系")
                //.data(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
                .data(cpuUsageList)
                .itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
        option.series(line);
        option.exportToHtml("line5.html");
        option.view();
    }
}