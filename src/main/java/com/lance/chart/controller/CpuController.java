package com.lance.chart.controller;

import com.lance.chart.runner.DrawChart;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cpu")
public class CpuController {

    @Autowired
    private DrawChart drawChart;

    @RequestMapping(value = "/option", method = RequestMethod.GET)
    public String getOption() throws ParseException {

        //以当前时间为截止时间，获取前3分钟数据
        Date endDate = new Date();
        Integer width = 3;
        return drawChart.draw(endDate, width);
    }

    @RequestMapping(value = "/base64", method = RequestMethod.POST)
    public void saveFile(@RequestParam Map<String, String> base64Info) throws IOException {
        log.info("save file controller");
        String picInfo = base64Info.get("picInfo").replace("data:image/png;base64,", "");
        byte[] buffer = Base64.decodeBase64(picInfo);
        String fileName = System.currentTimeMillis() + "-test.png";
        OutputStream out = new FileOutputStream("d://echart//" + fileName);
        out.write(buffer);
        out.flush();
        out.close();
    }

}
