package com.lance.chart.runner;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Line;
import com.lance.chart.dao.CpuDAO;
import com.lance.chart.entity.CPUEntity;
import com.lance.chart.util.EnhancedOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 获取渲染 echart 的 option json 数据，html 页面直接 setOption 即可展示 echart
 */
@Slf4j
@Component
public class DrawChart {

    @Autowired
    private CpuDAO cpuDAO;

    /**
     * 每次读取以 endDate 为截止时间的前 width 分钟的数据，并以 echarts option json 字符串格式返回
     *
     * @param endDate 截止时间
     * @param width 获取截止时间前多少分钟数据
     * @return
     * @throws ParseException
     */
    public String draw(Date endDate, Integer width) throws ParseException {

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
        //categoryAxis.axisLabel().formatter("{value}");
        categoryAxis.boundaryGap(false);
        categoryAxis.setName("时间");

        //获取起始时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);

        //根据 width 计算出 startDate
        calendar.add(Calendar.MINUTE, width*-1);
        Date startDate = calendar.getTime();

        //从数据库查询 某个时间段的 cpu 使用率
        List<CPUEntity> cpuEntities = cpuDAO.findCPUEntityByDate(startDate, endDate);

        DateFormat df = DateFormat.getTimeInstance();//x 轴只显示时分秒

        //x 轴数据
        Object[] dateList = cpuEntities.stream().map(item -> df.format(item.getCreateDate())).toArray();

        //y 轴数据
        Object[] cpuUsageList = cpuEntities.stream().map(item -> item.getCpuPercent()).toArray();

        //x 轴设置
        categoryAxis.data(dateList);
        //xValueAxis.data(dateList);
        option.xAxis(categoryAxis);

        Line line = new Line();

        //Symbol symbol = Symbol.circle;
        line.smooth(true).name("cpu 使用率与时间的变化关系")
                //.symbol(Symbol.circle)
                .symbolSize(8)
                .data(cpuUsageList)
                .itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
        option.series(line);

        //option.exportToHtml("line5.html");
        //option.view();

        String optionStr = option.toString();

        return optionStr;

    }
}