//
//  NTESDemoConfig.m
//  NIMAudioChatroom
//
//  Created by Simon Blue on 2019/1/16.
//  Copyright © 2019年 netease. All rights reserved.
//

#import "NTESDemoConfig.h"

@implementation NTESDemoConfig

+ (instancetype)sharedConfig
{
    static NTESDemoConfig *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[NTESDemoConfig alloc] init];
    });
    return instance;
}

- (instancetype)init
{
    if (self = [super init])
    {
        /*测试环境
        _appKey = @"0fbb6ba3ef9a98004153e3ff75ef1a2d";
        _apiURL = @"http://apptest.netease.im:8080/appdemo/voicechat";
         */
        
        /*线上环境*/
        _appKey = @"5887359c380d534ad99b33a07d8723e5";
        _apiURL = @"https://app.yunxin.163.com/appdemo/voicechat";
    }
    return self;
}

- (NSString *)appKey
{
    return _appKey;
}

- (NSString *)apiURL
{
    return _apiURL;
}

- (NSString *)cerName
{
    return nil;
}


@end
