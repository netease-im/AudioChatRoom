//
//  NTESJsonUtil.m
//  NIMAudioChatroom
//
//  Created by Simon Blue on 2019/1/24.
//  Copyright © 2019年 netease. All rights reserved.
//

#import "NTESJsonUtil.h"

@implementation NTESJsonUtil
+ (nullable NSDictionary *)dictByJsonData:(NSData *)data
{
    NSDictionary *dict = nil;
    if ([data isKindOfClass:[NSData class]])
    {
        NSError *error = nil;
        dict = [NSJSONSerialization JSONObjectWithData:data
                                               options:0
                                                 error:&error];
    }
    return [dict isKindOfClass:[NSDictionary class]] ? dict : nil;
}


+ (nullable NSDictionary *)dictByJsonString:(NSString *)jsonString
{
    if (!jsonString.length) {
        return nil;
    }
    NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    return [NTESJsonUtil dictByJsonData:data];
}

@end
