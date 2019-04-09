## 网易云信多人语音聊天室解决方案iOS端实现说明

### 一）终端整体业务逻辑简介

#### 1. 解决方案概述
	
多人语音聊天室是社交娱乐产品中常见的纯音频场景，支持1个主播和多个连麦者的语音互动以及多人聊天室的文字互动，并可以在此基础上搭建多主播PK、在线KTV等多种玩法。该解决方案采用了网易云信音视频通话方案，具有实时性高、接入方便等优势，同时方案中还集成了可靠的IM聊天室能力，方便快速实现消息互动、表情发送、麦位管理、队列管理等功能。

#### 2. 解决方案角色简介

多人语音聊天室Demo的场景里有三类角色：

* 主播：房间创建者，始终处于上麦状态，可实现抱麦、禁言、禁音、踢人、播放音乐等功能
* 连麦者：互动观众，可通过主动申请和被主播抱麦两种形式上麦，上麦后可与主播和其他连麦者进行语音活动
* 普通观众：非互动观众，无法进行语音互动，只能接收语音数据和进行文字互动，连麦者和普通观众可互相切换

#### 3. 注意事项

该解决方案使用了云信即时通信和音视频通话SDK，因此在使用本解决方案之前请务必了解：

* [即时通信的聊天室能力](https://dev.yunxin.163.com/docs/product/IM即时通讯/产品介绍/主要功能?kw=聊天室&pg=1&pid=0#聊天室功能)
* [音视频通话能力](https://dev.yunxin.163.com/docs/product/音视频通话/新手接入指南)

### 二) 解决方案重难点逻辑实现

#### 1. 申请上麦的队列管理

观众针对特定麦位发起动作事件时，解决方案内部采用自定义的P2P通知方式，需要将相关自身信息和待操作的麦位信息通过通知传递给主播，主播根据相对应地麦位做出麦位状态管理。主要有以下几种事件：

| 事件类型 | 事件发送方向 | 事件发送时机 | 主播接收事件的可能响应 |
| ------- | ----------| ----------| ------------------|
| 申请连麦</br>(value = 1)| 观众 -> 主播 | 1. 当前是观众身份 </br> 2.点击无人麦位(未关闭) | 1.接受，麦位状态改为上麦，理由为通过</br> 2.拒绝，麦位状态更改为空，理由为拒绝|
| 主动下麦</br>(value = 2) | 连麦者 -> 主播 | 1. 当前是连麦者身份 | 麦位状态切换至无人</br>(NTESMicStatusNone)|
| 取消申请</br>(value = 3) | 观众 -> 主播 | 1. 观众身份 </br> 2.申请上麦状态 | 麦位状态切换至无人</br>(NTESMicStatusNone)|

#### 2. 麦位管理

主播在收到观众端进行的申请或者主动发起事件后，需要对相应的麦位进行状态变更。解决方案的麦位状态管理，通过服务端维护的元素队列来达到所有观众的麦位状态同步。

| 麦位状态 | 说明 |
| ------- | ------ |
| NTESMicStatusNone</br>(value = 0) | 麦位无人 |
| NTESMicStatusConnecting</br>(value = 1) | 正在申请 |
| NTESMicStatusConnectFinished</br>(value = 2) | 申请完成上麦 |
| NTESMicStatusClosed</br>(value = 3) | 麦位被关闭 |
| NTESMicStatusMasked</br>(value = 4) | 麦位无人被屏蔽 |
| NTESMicStatusConnectFinishedWithMasked</br>(value = 5) | 麦位有人被屏蔽 |
| NTESMicStatusConnectFinishedWithMuted</br>(value = 6) | 麦位有人但是连麦者关闭了话筒 |
| NTESMicStatusConnectFinishedWithMutedAndMasked</br>(value = 7) | 麦位有人，连麦者关闭了话筒，被屏蔽 

在主播将麦位更新到相应状态的同时还会带上原因，用来表明状态切换的原因。观众需要根据更新后的状态和原因来更新自身的状态。

| 麦位状态切换原因 | 说明 |
| ------- | ------ |
| NTESMicReasonNone</br>(value = 0) | 无原因 |
| NTESMicReasonConnectAccepted</br>(value = 1) | 被同意上麦 |
| NTESMicReasonConnectInvited</br>(value = 2) | 被抱麦 |
| NTESMicReasonMicKicked</br>(value = 3) | 被踢 |
| NTESMicReasonDropMic</br>(value = 4) | 主动下麦 |
| NTESMicReasonCancelConnect</br>(value = 5) | 用户取消连麦 |
| NTESMicReasonConnectRejected</br>(value = 6) | 被拒绝 |
| NTESMicReasonMicMasked</br>(value = 7) | 上麦之前被屏蔽 |
| NTESMicReasonMicMasked</br>(value = 8) | 恢复语音 |
| NTESMicReasonMicMasked</br>(value = 9) | 打开麦位 |

例如用户申请上麦，主播收到后批准这个业务，主要交互流程如下：

* 观众 -> 主播。发送请求上麦通知，包括目标麦位和个人信息，以及状态切换申请。
* 主播 -> 观众。主播收到后，将状态切换为NTESMicStatusConnecting，并更新队列。此时所有观众都可以收到队列更新的回调，更新当前麦位的最新状态。
* 主播 -> 观众。主播允许上麦。将状态切换为NTESMicStatusConnectFinished，原因是NTESMicReasonConnectAccepted，并更新队列。此时所有观众同步更新当前麦位的状态，申请者判断是自己被上麦后，改变在音视频房间的身份（setMeetingRole:），完成上麦。

#### 3. 房间管理

解决方案的房间管理是通过应用服务器来进行管理的，同时应用服务器还提供房间禁言等接口，以供终端来进行相应的效果控制。涉及到的服务端接口如下：

| 接口 | 功能 | 说明 |
| --- | ---- | ---- |
| NTESDemoAccountTask |  申请账号和token | IM账号由应用服务端管理，所有功能都需要在登陆IM账号完成后使用 |
| NTESDemoChatroomListTask | 查询房间列表 | 当前所有的音视频房间列表 |
| NTESDemoCreateChatroomTask | 创建新房间 | 应用服务端创建新的聊天室，有效期48h. |
| NTESDemoCloseChatroomTask | 解散房间 | 应用服务端解散新房间，客户端在主播退出房间后调用。|
| NTESDemoMuteAllTask | 全员禁言 | 聊天室禁言属于高级接口，由应用服务器负责调用。|

**注**：创建房间和解散房间指的是IM的聊天室，音视频的会议由客户端申请，当会议中所有人都离开后，会议自动解散。

#### 4. 伴音功能

解决方案的伴音功能主要是针对主播端的，主播在音频连麦的过程中可以将伴音音频一同发送给房间里所有的用户。主要涉及的伴音功能接口如下([NIMAVChatSDK sharedSDK].netCallManager)：

| 接口 | 功能 |
| --- | ---- |
| startAudioMix |  开始播放伴音 |
| stopAudioMix |  停止播放伴音 |
| pauseAudioMix |  暂停播放伴音 |
| resumeAudioMix |  恢复播放伴音 |

### 三）源码导读

#### 1. 工程说明

本解决方案Demo基于以下开发：

* 项目依赖管理 [CocoaPods](https://cocoapods.org/)，版本1.6.0
* 网易云信完整版本 [NIMSDK](http://netease.im/im-sdk-demo)，版本6.2.0
* 网络连接状态检测库 [Reachability](https://github.com/tonymillion/Reachability), 版本3.1.1
* 加载状态UI [SVProgressHUD](https://github.com/SVProgressHUD/SVProgressHUD), 版本2.2.5
* 富文本UI [M80AttributedLabel](https://github.com/xiangwangfeng/M80AttributedLabel), 版本1.3.1
* 网络图片加载库 [YYWebImage](https://github.com/ibireme/YYWebImage), 版本1.0.5
* 下拉刷新UI [MJRefresh](https://github.com/CoderMJLee/MJRefresh), 版本3.1.15.7

#### 2. 工程结构

#####2.1 文件目录图
![](http://yx-web.nos.netease.com/webdoc/default/audiochatroom_dir.png)

#####2.2 工程图
![](http://yx-web.nos.netease.com/webdoc/default/audiochatroom_project.png)

#####2.3 工程介绍
```
└── NIMAudioChatroom/Classes                   # 多人语音聊天室工程
    ├── Sections                               # 业务单元
    │   └── HomePage                           # 首页业务
    │   └── Chatroom                           # 聊天室业务
    │   │   └── DataSource                     # 聊天室相关的数据管理
    │   │   └── AudioPlayer                    # 伴音业务
    │   │   └── Handler                        # IM事件回调
    │   │   └── View                           # UI控件
    │   └── SelectList                         # 选择列表业务
    ├── Service                                # 网络请求
    ├── Model                                  # 数据模型
    ├── Logger                                 # 日志
    ├── Util                                   # 通用工具/常规配置
    └── Category                               # Category 工具
```

#### 3. 网络层介绍

所有的网络请求均封装为遵循NTESDemoServiceTask协议的Task（详见2.3列表）,通过NTESDemoService管理类进行网络请求。目前有如下网络请求：

#####3.1 获取IM账号

* 请求说明

	```
	POST http://${Host}/user/get HTTP/1.1
	Content-Type: application/x-www-form-urlencoded;charset=utf-8
	```
* 参数说明

	| 参数名称 | 参数类型 | 是否必填 | 参数说明 |
	| --- | ---- | ---- | ---- |
	| sid | String | 否 | Session Id，为了简化登录机制，sid等于用户账号accid |
	
* 返回值

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

#####3.2 查询房间列表

* 请求说明

	```
	POST http://${Host}/room/list HTTP/1.1
	Content-Type: application/x-www-form-urlencoded;charset=utf-8
	```
* 参数说明

	| 参数名称 | 参数类型 | 是否必填 | 参数说明 |
	| --- | ---- | ---- | ---- |
	| limit | int | 否 | 分页大小，默认20 |
	| offset | int | 否 | 分页偏移量，默认0 |

* 返回值

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

#####3.3 创建房间

* 请求说明

	```
	POST http://${Host}/room/create HTTP/1.1
	Content-Type: application/x-www-form-urlencoded;charset=utf-8
	```
* 参数说明

	| 参数名称 | 参数类型 | 是否必填 | 参数说明 |
	| --- | ---- | ---- | ---- |
	| sid | String | 是 | 获取账号后的sid |
	| roomName | String | 是 | 房间名称，1-16字符，只支持中文、数字和字母 |

* 返回值

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

#####3.4 解散房间

* 请求说明

	```
	POST http://${Host}/room/dissolve HTTP/1.1
	Content-Type: application/x-www-form-urlencoded;charset=utf-8
	```
* 参数说明

	| 参数名称 | 参数类型 | 是否必填 | 参数说明 |
	| --- | ---- | ---- | ---- |
	| sid | String | 是 | 获取账号后的sid |
	| roomId | long | 是 | 聊天室房间id |

* 返回值

	```
	{
    	"code": 200
	}
	```

#####3.4 全员禁言

* 请求说明

	```
	POST http://${Host}/room/mute HTTP/1.1
	Content-Type: application/x-www-form-urlencoded;charset=utf-8
	```
* 参数说明

	| 参数名称 | 参数类型 | 是否必填 | 参数说明 |
	| --- | ---- | ---- | ---- |
	| sid | String | 是 | 获取账号后的sid |
	| roomId | long | 是 | 聊天室房间id |
	| mute | boolean | 是 | 聊天室房间id |
	| needNotify | boolean | 否 | 是否通知，默认true |
	| notifyExt | boolean | 否 | 是否通知扩展字段，默认false |

* 返回值

	```
	{
    	"code": 200
	}
	```

#### 4. 业务层介绍

多人语音聊天室的主类是NTESChatroomViewController，主要的交互业务在Sections/Chatroom/Handler，其中主要的模块如下：

1. 自定义通知发送：NTESCustomNotificationHelper
2. 自定义通知接收解析：NTESChatroomNotificationHandler
3. 队列元素更新：NTESChatroomQueueHelper