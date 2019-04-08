网易云信实现说明
# 1. 概述
 多人语音聊天室是社交娱乐产品中常见的纯音频场景，支持1个主播和多个连麦者的语音互动以及多人聊天室的文字互动，并可以在此基础上搭建多主播PK、在线KTV等多种玩法。该解决方案采用了网易云信音视频通话方案，具有实时性高、接入方便等优势，同时方案中还集成了可靠的IM聊天室能力，方便快速实现消息互动、表情发送、麦位管理、队列管理等功能。
# 2.工程结构说明
## 2.1 技术框架
* 项目依赖管理 Maven
* Spring MVC 4.3.0.RELEASE
* Spring Session 1.2.2.RELEASE
* 数据库持久层框架 Mybatis 3.4.4
* redis客户端 Redisson 1.2.1
* Java Bean映射框架 Mapstruct 1.1.0.Final
* Json处理框架 Fastjson 1.2.33
* 日志框架 Slf4j 1.7.25 + Logback 1.2.3
## 2.2 逻辑架构
* demo-server 模块，工程部署入口，包含权限校验拦截器、工程配置文件定义、全局异常处理等
* demo-module 模块，对外接口定义，分为主持人相关接口，以及玩家相关接口两部分
* demo-common-service 模块，业务逻辑实现层
* demo-common-dao 模块，数据依赖，包含云信Api调用，数据库和缓存调用
* demo-common 模块，工程常量定义以及工具类实现
# 3. 部署说明
## 3.1 前置条件
### 3.1.1 Mysql数据库配置
数据库配置完成后，需要确定如下配置：
* 数据库地址：假设为127.0.0.1:3306
* 数据库名称：默认设置为appdemo
* 数据用户名：默认设置为demouser
* 数据库密码：默认设置为demopass
* 创建数据表：demo_voice_material,demo_voice_room,demo_voice_tourist

具体操作如下：
* 确保已安装Mysql数据库（推荐版本5.5+）
* 创建数据库以及数据库用户（如果已有配置，可跳过，直接进行下一步）
    ``` 
    # 创建数据库，数据库名称可自定义
     CREATE DATABASE `appdemo` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
     # 创建数据库用户
     CREATE USER 'demouser'@'%' IDENTIFIED BY 'demopass';
     # 赋予用户数据库操作权限
     GRANT ALL ON appdemo.* TO 'demouser'@'%';
    ```
    **注意：为了简化和统一配置，数据库用户host设置为'%'，并且赋予了appdemo库的所有权限，安全级别较低，生产环境请自定义配置**

* 创建数据表
    ```
     # 建表语句
     use appdemo;
     
     # 语音聊天室素材表
     CREATE TABLE `demo_voice_material` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
      `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '素材类型，0-聊天室房间封面，1-用户头像',
      `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已删除',
      `url` varchar(255) DEFAULT NULL COMMENT '素材url',
      `created_at` timestamp NOT NULL DEFAULT '2019-01-01 00:00:00' COMMENT '创建时间\n',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='语音聊天室素材';
     
     # 语音聊天室房间信息表
     CREATE TABLE `demo_voice_room` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
      `room_id` bigint(20) unsigned NOT NULL COMMENT '聊天室房间号',
      `creator` varchar(64) NOT NULL COMMENT '房主账号',
      `name` varchar(128) NOT NULL COMMENT '房间名称',
      `thumbnail` varchar(255) DEFAULT NULL COMMENT '房间缩略图',
      `valid` bit(1) NOT NULL DEFAULT b'0' COMMENT '房间是否有效，0-不是；1-是',
      `visible` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否对外可见，0-不是；1-是',
      `created_at` timestamp NOT NULL DEFAULT '2018-01-01 08:00:00' COMMENT '创建时间',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`),
      UNIQUE KEY `uk_roomid` (`room_id`),
      KEY `idx_owner` (`creator`)
    ) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COMMENT='语音聊天室房间信息表';
     
     # 语音聊天室游客账号表
     CREATE TABLE `demo_voice_tourist` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
      `accid` varchar(64) NOT NULL COMMENT '游客账号',
      `nickname` varchar(64) NOT NULL COMMENT '游客昵称',
      `icon` varchar(255) DEFAULT NULL COMMENT '用户头像',
      `im_token` varchar(64) DEFAULT NULL COMMENT 'im token',
      `vod_token` varchar(64) DEFAULT NULL COMMENT '点播token',
      `available_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '游客账号被释放的毫秒时间戳',
      `created_at` timestamp NOT NULL DEFAULT '2018-01-01 00:00:00' COMMENT '创建时间',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`),
      UNIQUE KEY `idx_accid` (`accid`),
      KEY `idx_available_at` (`available_at`)
    ) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8mb4 COMMENT='语音聊天室游客账号';
    ```
### 3.1.2 Redis配置
确定redis配置信息：
+ redis地址：假设为127.0.0.1:6379
+ redis密码：假设未设置密码
### 3.1.3 云信App配置
从[云信控制台](https://app.netease.im/index#/)获取应用信息，假设为：
+ App Key: bc01d41ebc78d6v5kn23a83d33f08n9c
+ App Secret: cl2k6c7p3xh0
确保应用开通以下功能：
+ 实时音视频
+ 聊天室
### 3.1.4 Maven配置
确保服务器已安装 Maven
执行 mvn -v命令能看到 maven相关信息输出
    ```
    $ mvn -v
    Apache Maven...
    Maven home...
    ```
## 3.2 工程配置
配置文件根据环境不同，位于不同的路径下，部署时通过指定 Maven Profile 属性，使用指定环境的配置文件。目前已有配置文件的路径如下，其中dev表示开发环境，test表示测试环境，pre表示预发步环境，prod表示线上生产环境。相关的Maven配置位于工程目录demo-server/pom.xml文件的profiles节点中，默认启用开发环境dev。
   ```
demo-server/src/main/resources/profile
├── dev
│   ├── config.properties
│   ├── db.properties
│   └── logback.xml
├── pre
│   ├── config.properties
│   ├── db.properties
│   └── logback.xml
├── prod
│   ├── config.properties
│   ├── db.properties
│   └── logback.xml
└── test
    ├── config.properties
    ├── db.properties
    └── logback.xml
```

+ `config.properties`文件配置
	- 云信服务端api接口地址配置：`nim.server.api.url=https://api.netease.im/nimserver/`
	- AppKey配置：`appkey=bc01d41ebc78d6v5kn23a83d33f08n9c`
	- AppSecret配置：`appsecret=cl2k6c7p3xh0`
+ `db.properties`文件配置
	- Mysql配置  
	
		```
		mysql.driver=com.mysql.jdbc.Driver
		mysql.url=jdbc:mysql://127.0.0.1:3306/appdemo?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
		mysql.user=demouser
		mysql.pwd=demopass
		```
	- Redis配置
	
		```
		redis.ip=127.0.0.1
		redis.port=6379
		redis.password=
		```
+ `logback.xml`文件配置
	- 指定工程日志路径  
		默认配置为：`<property name="log.dir" scope="context" value="${catalina.home}/logs"/>`
		可以根据需要自定义工程日志路径
## 3.3 项目部署
+ 通过war包部署  
	切换到工程根目录下，执行以下操作：
	
	```
	# 从父工程打包，使用开发环境配置文件，跳过单元测试
	$ mvn clean install -Pdev -Dmaven.test.skip=true
	```
	打包成功后，会生成war包 `demo-server/target/appdemo.war`  
	接下来就可以将war包发布到已有tomcat的`webapps`目录下进行部署
+ 直接通过`maven tomcat`插件部署  
	项目父工程`pom.xml`文件中已经定义好了插件配置，默认部署到tomcat根目录下，并指定端口`8088`。
	
	```
    <plugin>
        <groupId>org.apache.tomcat.maven</groupId>
        <artifactId>tomcat7-maven-plugin</artifactId>
        <version>2.1</version>
        <configuration>
            <path>/</path>
            <port>8088</port>
            <charset>UTF-8</charset>
            <uriEncoding>UTF8</uriEncoding>
        </configuration>
    </plugin>
	```
	切换到工程根目录下，执行以下操作：
	
	```
	# 从父工程打包，使用开发环境配置文件，跳过单元测试
	$ mvn clean install -Pdev -Dmaven.test.skip=true
	
	# 切换到 demo-server 模块
	$ cd demo-server
	
	# 通过tomcat插件启动
	$ mvn tomcat7:run
	```

# 4. 接口描述
## 4.1. 查询房间列表
### 4.1.1 请求说明
```
POST http://${Host}/room/list HTTP/1.1Content-Type: application/x-www-form-urlencoded;charset=utf-8
```
### 4.1.2 参数说明
| 参数名称 | 参数类型 | 是否必填 | 参数说明 |
|---|----|----|---|
|limit| int |否| 分页大小，默认20|
|offset |int |否 |分页偏移量，默认0|
### 4.1.3 返回值
```
{
    "code": 200, 
    "data": {
        "total": 1, 
        "list": [
            {
                "roomId": 60935727, 
                "name": "临湖小筑", 
                "creator": "user56977", 
                "thumbnail": "https://s2.ax1x.com/2019/01/15/FzNb5V.png", 
                "onlineUserCount": 0, 
                "createTime": 1547134737215
            }
        ]
    }
}
```
## 4.2. 获取IM账号
### 4.2.1 请求说明
```
POST http://${Host}/user/get HTTP/1.1Content-Type: application/x-www-form-urlencoded;charset=utf-8
```
### 4.2.2 参数说明
|参数名称 |参数类型 |是否必填 |参数说明|
|---|----|----|---|
|sid| String| 否| Session Id，为了简化登录机制，sid等于用户账号accid|
接口请求时携带sid，只是说明客户端期望继续使用当前账号，服务器会判断当前账号是否已过期，如果当前账号已过期会重新分配账号。
所以实际登录用户，以接口返回值为准，客户端注意进行切换，如果继续使用已过期账号，可能出现账号冲突。
### 4.2.3 返回值
```
{
    "code": 200,
    "data": {
        "sid": "user11850333", // sid
        "accid": "user11850333", // 用户名
        "nickname": "用户468820", // 昵称
        "icon": "https://s2.ax1x.com/2019/01/15/FzUSbR.png",
        "imToken": "b4b7a5e1730fcd51c1b0ca843640fe9b", // im token
        "availableAt": 1547155366875 // 账号过期时间
    }
}
```
### 4.2.4 错误码
900、911
## 4.3. 创建房间
### 4.3.1 请求说明
```
POST http://${Host}/room/create HTTP/1.1Content-Type: application/x-www-form-urlencoded;charset=utf-8
```
同一个sid只允许存在一个房间，所以在创建房间时，会强制解散相同sid之前创建的房间
### 4.3.2 参数说明
|参数名| 参数类型 |是否必填| 参数说明|
|---|----|----|---|
|sid| String| 是| 获取账号后的sid|
roomName String 是 房间名称，1-16字符，只支持中文、数字和字母
### 4.3.3 返回值
```
{
    "code": 200,
    "data": {
        "roomId": 60935727,
        "creator": "user11850333",
        "name": "临湖小筑",
        "thumbnail": "https://s2.ax1x.com/2019/01/15/FzNb5V.png",
        "createTime": 1547134737215
    }
}
```
### 4.3.4 错误码
400、401、800
## 4.4. 全员禁言
### 4.4.1 请求说明
```
POST http://${Host}/room/mute HTTP/1.1Content-Type: application/x-www-form-urlencoded;charset=utf-8
```
### 4.4.2 参数说明
|参数名| 参数类型 |是否必填| 参数说明|
|---|----|----|---|
|sid| String| 是| 获取账号后的sid|
|roomId |long |是| 聊天室房间id|
|mute boolean| 是 |true-禁用，false-解除禁言|
|needNotify |boolean ｜否 |是否通知，默认true|
|notifyExt|boolean| 否| 是否通知扩展字段，默认false|
### 4.4.3 返回值
```
{
    "code": 200
}
```
### 4.4.4 错误码
400、401、403、800、801、804
## 4.5. 解散房间
### 4.5.1 请求说明
```
POST http://${Host}/room/dissolve HTTP/1.1Content-Type: application/x-www-form-urlencoded;charset=utf-8
```
### 4.5.2 参数说明
|参数名称| 参数类型 |是否必填| 参数说明|
|---|----|----|---|
|sid |String |是 |获取账号后的sid|
|roomId| long| 是| 聊天室房间id|
### 4.5.3 返回值
```
{
    "code": 200
}
```
### 4.5.4 错误码
400、401、403、800、801、804
## 4.6. 清理过期房间
### 4.6.1 请求说明
```
POST http://${Host}/task/invalidTimeoutRoom HTTP/1.1Content-Type: application/x-www-form-urlencoded;charset=utf-8
```
解散任务执行时刻，${timeoutHours}小时之前创建的房间，该接口有ip白名单，只允许服务器主机调用
### 4.6.2 参数说明
|参数名称| 参数类型| 是否必填| 参数说明|
|---|----|----|---|
|timeoutHours| int| 否| 房间失效时长，默认为48小时|
### 4.6.3 返回值
```
{
    "code": 200,
    "data": "dissolve 5 rooms"
}
```
### 4.6.4 错误码
400、403
## 4.7. 错误码表
|错误码| 含义|
|---|----|
|400| 请求参数错误|
|401| 用户校验失败|
|403| 非法操作|
|800 |房间请求类错误（服务器内部）|
|801| 房间已解散|
|804 |房间不存在|
|900| 用户请求类错误（服务器内部）|
|911 |同一IP当天获取账号频控（办公网加白）|
