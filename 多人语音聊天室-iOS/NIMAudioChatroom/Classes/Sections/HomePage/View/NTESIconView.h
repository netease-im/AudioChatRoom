//
//  NTESIconView.h
//  NIMAudioChatroom
//
//  Created by Netease on 2019/3/4.
//  Copyright © 2019年 netease. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface NTESIconView : UIView

- (void)setName:(NSString *)name
        iconUrl:(NSString *)iconUrl;

@property (nonatomic, strong) UIColor *nameColor;

@property (nonatomic, assign) BOOL mute;

- (void)startAnimation;

- (void)stopAnimation;

@end

NS_ASSUME_NONNULL_END
