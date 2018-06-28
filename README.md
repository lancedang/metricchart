##### metricchart 项目背景
spring boot + jpa + echarts + phantomjs + 本地保存图片

##### 前提
- 运行 echarts 源文件 src/java/test/sample/lines example 如LineTest5， git clone 地址： https://gitee.com/free/ECharts.git
- test 环境可以直接展现 html 页面（见 参考1）

##### 项目构建流程
1. 通过 echart-java （见参考1） 后台创建 echart 所需 option json string
2. 创建 index.html 文件，作为前台页面展现 echart 图表，前台通过 jQuery 发送异步get 请求，用以得到 1 中的 option string（见参考2，可 scroll 异步加载模块）
3. http://localhost:8200 页面查看效果, 下一步，保存 html echart 到 png
4. 引入 phantomjs， 本地安装 phantomjs-2.1.1 （见参考 3）


##### 使用说明
- 应用启动后可在 http://localhost:8200 查看页面效果, 默认 打开index.html
- 应用启动后，在 d://echart 目录生成 **.png 图片，提前创建 echart 目录，关闭应用打开图片

##### 核心原理
- echart-java 直接生成了 html 所需的所有 option json string, 直接 setOption(optionJsonString) 即可展现
- phantomjs 后台模拟 html，并通过 png url 获取 base64 img 二进制文件，然后保存即可

##### 实时保存图片的思路（todo）
- index 页面通过 getImage 实时（每隔 3 秒）向后台发送 png 的 base64 数据，后台解析 base64 并 iO 到 png 文件中, 这样不需要 phantomjs 了 

##### 参考
1. https://github.com/abel533/ECharts
2. http://echarts.baidu.com/tutorial.html#5%20%E5%88%86%E9%92%9F%E4%B8%8A%E6%89%8B%20ECharts
3. https://blog.csdn.net/aofavx/article/details/80395833
4. http://echarts.baidu.com/echarts2/doc/doc.html
5. http://phantomjs.org/ 注：可具体参考其中的 "quick start" 和 "more examples" 连接
