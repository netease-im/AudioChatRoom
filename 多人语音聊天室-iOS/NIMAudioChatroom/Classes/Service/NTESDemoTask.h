//
//  NTESDemoTask.h
//  NIMAudioChatroom
//
//  Created by Simon Blue on 2019/1/15.
//  Copyright © 2019年 netease. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NTESDemoServiceTask.h"

@class NTESAccountInfo;
@class NTESChatroomInfo;

typedef void(^NTESCommonHandler)(NSError *error);

//获取IM账号
typedef void(^NTESAccountHandler)(NTESAccountInfo *accountInfo, NSError *error);
@interface NTESDemoAccountTask : NSObject<NTESDemoServiceTask>
@property (nonatomic, copy, nullable)NSString *sid;
@property (nonatomic, copy)NTESAccountHandler handler;
@end

//查询房间列表
typedef void(^NTESChatroomHandler)(NSMutableArray <NTESChatroomInfo *> *chatroomInfos, NSError *error);
@interface NTESDemoChatroomListTask : NSObject<NTESDemoServiceTask>
@property (nonatomic, assign)NSInteger limit;
@property (nonatomic, assign)NSInteger offset;
@property (nonatomic, copy)NTESChatroomHandler handler;
@end

//创建房间
typedef void(^NTESCreateChatroomHandler)(NTESChatroomInfo *chatroomInfo, NSError *error);
@interface NTESDemoCreateChatroomTask : NSObject<NTESDemoServiceTask>
@property (nonatomic, copy)NSString *sid;
@property (nonatomic, copy)NSString *roomName;
@property (nonatomic, copy)NTESCreateChatroomHandler handler;
@end

//解散房间
@interface NTESDemoCloseChatroomTask : NSObject<NTESDemoServiceTask>
@property (nonatomic, copy)NSString *sid;
@property (nonatomic, assign)NSInteger roomId;
@property (nonatomic, copy)NTESCommonHandler handler;
@end

//全员禁言
@interface NTESDemoMuteAllTask : NSObject<NTESDemoServiceTask>
@property (nonatomic, copy) NSString *sid; //获取账号后的sid
@property (nonatomic, assign) NSInteger roomId; //聊天室房间id
@property (nonatomic, assign) BOOL mute; //true-禁用，false-解除禁言
@property (nonatomic, assign) BOOL needNotifiy; //是否通知，默认true
@property (nonatomic, assign) BOOL notifyExt; //是否通知扩展字段，默认false
@property (nonatomic, copy)NTESCommonHandler handler;
@end
