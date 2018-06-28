package com.lance.chart.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 获取 option json 字符串填充 html, 然后 保存图片, 用 plattomjs 模拟 html 网页, 从而达到不开启浏览器却能读取dom 的目的
 */
@Slf4j
@Component
public class SaveFileUtil {

    /**
     * 将 html echart 图片保存到本地, 默认保存到 d://echart/fileName.png, 需提前建立 echart 目录
     * @param optionJsonStr, echart option json string
     * @param fileName, fileName 如 chart-demo
     * @return
     * @throws IOException
     */
    public List<String> saveHtmlEchartToLocalImg(String optionJsonStr, String fileName) throws IOException {
        List<String> imageBase64List = new ArrayList<String>();

        PhantomJSDriver driver = getPhantomJSDriver();
        // 让浏览器访问空间主页
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //driver.get("http://testurl.com/file/upload/saveEchartHtml.html");
        driver.get("http://localhost:8200/index.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        //设置option数据到页面
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        log.info("save file option string: " + optionJsonStr);

        //展示echarts
        js.executeScript("showImg(" + optionJsonStr + ")");

        //获取echart图片数据
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        String temTxt = (String) js.executeScript("return returnEchartImg(myChart)");

        //imageBase64放到list中
        imageBase64List.add(temTxt.replace("data:image/png;base64,", ""));

        // 数据中:data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABI4AAAEsCAYAAAClh/jbAAA ...在"base64,"之后的才是图片信息
        //String[] arr = base64Info.split("base64,");
        // 将图片输出到系统某目录.
        OutputStream out = null;
        log.info("base: " + imageBase64List.get(0));
        // 使用了Apache commons codec的包来解析Base64
        byte[] buffer = Base64.decodeBase64(imageBase64List.get(0));

        out = new FileOutputStream("d://echart//" + fileName+ ".png");
        out.write(buffer);
        out.flush();
        //ImgFileUtils.GenerateImage(phTxt, "d://ph01.png");
        driver.close();
        driver.quit();
        return imageBase64List;
    }


    private PhantomJSDriver getPhantomJSDriver() {
        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持
        String osname = System.getProperties().getProperty("os.name");
        if (osname.equals("Linux")) {//判断系统的环境win or Linux
            dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/bin/phantomjs");
        } else {
            dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "D:\\phantomjs-2.1.1\\bin\\phantomjs.exe");
        }
        //创建无界面浏览器对象
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
        return driver;
    }
}
