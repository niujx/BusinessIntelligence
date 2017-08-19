# 商户爬虫平台说明文档


### 部署和环境说明

- 使用技术
  - spring-boot mybatis httpclent

- 项目地址 git

   - https://github.com/niujx/BusinessIntelligence.git

- 部署环境

   - JDK1.8
   - MAVEN
   - 默认端口号 9578

-  开发环境
   - 建议使用idea
   - 安装lombok插件 必装否则项目报错

- 打包方式
   - mvn clean install -DskipTests

- 启动命令
   - java -jar   Business-Intelligence-1.0-SNAPSHOT.jar
   - 更换端口号 java -jar   Business-Intelligence-1.0-SNAPSHOT.jar --server.port=端口号

- 目录结构

  - 文件夹自动生成在jar所在的文件夹内
  - logs 日志文件存放处
  - cookieStore 存储cookies 对象序列化文件
  - ElemeKsidStore 存储饿了么认证文件
  - img  存放验证码图片的地址 建议定期清理
  - csv  存放下载excel csv 的路径仅限baidu

- 访问入口

  - http:ip:port/index.html
  - http:ip:port/swagger-ui.html  API文档服务 慎用


### 项目结构说明

-  项目模块说明

   - config 存放数据库配置类
   - controller restful api
   - cralwer 爬虫模块
     -  baidu 百度爬虫
     -  core 核心模块
     -  eleme 饿了么爬虫
     -  mt 美团爬虫
   - dao 数据库层 处理入库操作
   - model 爬虫数据的对象实体类
   - task  定时任务
   - util 工具类模块 包括操作http 验证码 cookies 等
   - Server 是服务入库类 调用main方法启动服务
   - mapper 存放sql的配置文件
   - public 存放web页面目录
       - css css样式表
       - fonts 字体文件
       - js  js脚本
       - page  存放页面
       - index.html 入口页面
    - templates 模板文件
    - application.yml 系统配置文件
    - logback.xml 日志配置文件
    - mybatis-config.xml mybatis配置文件
    - test 测试类
    - pom.xml 依赖配置

### 表文件说明

   - BIL_BD_HOT_DISHES 百度热门商品
   - BIL_BD_BOOKED_TABLE 百度现金账户流水明细
   - BIL_BD_BUSINESS_DATA 百度经营数据
   - BIL_BD_SHOP_WITHDRAWAL 百度商户提现表
   - BIL_BD_ORDER_DETAILS 百度订单详情表
   - BIL_BD_SHOP_COMMENT 百度商户评论列表
   - TB_CRAWLER_STATUS 爬虫状态表
   - BIL_ELEME_EVALUATE 饿了么顾客评价
   - BIL_ELEME_ACTIVITY 饿了么商店活动
   - BIL_ELEME_BILL 饿了么账单记录
   - BIL_ELEME_COMMODITY 饿了么商品分析
   - BIL_ELEME_FLOW 饿了么排名流量
   - BIL_ELEME_SALE 饿了么营业统计
   - BIL_ELEME_ORDER 饿了么订单数据
   - BIL_MT_ORDER 美团经营分析报表
   - BIL_MT_BUSINESS 美团营业统计
   - BIL_MT_ANALYSIS 美团营业统计流量分析
   - BIL_MT_SALES 美团热门商品
   - BIL_MT_COMMENT 美团评论
   - BIL_MT_BILL 美团财务管理
   - BIL_MT_ACT 美团活动
   - TB_ALL_MERCHANTINFO 商户表

### 其他说明
   - 修改 任务调度时间 在com.business.intelligence.task.CrawlerTasks 修改注解 @Scheduled(cron = "* * 16 * * *")
   - 日期填写格式yyyy-MM-dd  2017-01-01

   - 定要保证用户名、密码、shopid的准确性，如果有不正确的，程序可能无法正常执行，影响之后的操作。而且同一账号，多次出现的话可能出现提示账号风险，造成之后无法正常进行，如果出现此类情况，请过几天再试

   - 程序会放在一个文件夹中，除了jar还有几个文件夹是存放必要数据，请勿进行更改

