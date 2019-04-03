//
//  NTESAnimationButton.m
//  NIMAudioChatroom
//
//  Created by Netease on 2019/3/13.
//  Copyright © 2019年 netease. All rights reserved.
//

#import "NTESAnimationButton.h"

@interface NTESAnimationButton ()

@property (nonatomic, strong)CALayer *animationLayer;
@property (nonatomic, assign) BOOL isAnimating;

@end

@implementation NTESAnimationButton

- (void)startCustomAnimation {
    if (_isAnimating) {
        return;
    }
    if (!_animationLayer) {
        _animationLayer = [CALayer layer];
        NSMutableArray <CALayer *> *pulsingLayers = [self setupAnimationLayers:self.frame];
        __weak typeof(self) weakSelf = self;
        [pulsingLayers enumerateObjectsUsingBlock:^(CALayer * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [weakSelf.animationLayer addSublayer:obj];
        }];
        [self.layer addSublayer:_animationLayer];
    } else {
        _animationLayer.hidden = NO;
    }
    _isAnimating = YES;
}

- (void)stopCustomAnimation {
    if (!_isAnimating) {
        return;
    }
    _animationLayer.hidden = YES;
    _isAnimating = NO;
}

- (void)closeCustomAnimation {
    if (_animationLayer) {
        [_animationLayer.sublayers enumerateObjectsUsingBlock:^(__kindof CALayer * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [obj removeAllAnimations];
        }];
        [_animationLayer.sublayers makeObjectsPerformSelector:@selector(removeFromSuperlayer)];
        [_animationLayer removeFromSuperlayer];
        _animationLayer = nil;
    }
}

- (NSMutableArray <CALayer *> *)setupAnimationLayers:(CGRect)rect {
    NSMutableArray <CALayer *> *ret = [NSMutableArray array];
    NSInteger pulsingCount = 5;
    double animationDuration = 3;
    for (int i = 0; i < pulsingCount; i++) {
        CALayer *pulsingLayer = [CALayer layer];
        pulsingLayer.frame = CGRectMake(0, 0, rect.size.width, rect.size.height);
        pulsingLayer.borderColor = UIColorFromRGBA(0x35A4FF, 1.0).CGColor;
        pulsingLayer.borderWidth = 1;
        pulsingLayer.cornerRadius = rect.size.height/2;
        
        CAMediaTimingFunction *defaultCurve = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionDefault];
        CAAnimationGroup *animationGroup = [CAAnimationGroup animation];
        animationGroup.fillMode = kCAFillModeBackwards;
        animationGroup.beginTime = CACurrentMediaTime() + (double)i * animationDuration / (double)pulsingCount;
        animationGroup.duration = animationDuration;
        animationGroup.repeatCount = HUGE;
        animationGroup.timingFunction = defaultCurve;
        
        CABasicAnimation * scaleAnimation = [CABasicAnimation animationWithKeyPath:@"transform.scale"];
        scaleAnimation.fromValue = @1.0;
        scaleAnimation.toValue = @1.5;
        
        CAKeyframeAnimation * opacityAnimation = [CAKeyframeAnimation animationWithKeyPath:@"opacity"];
        opacityAnimation.values = @[@1, @0.9, @0.8, @0.7, @0.6, @0.5, @0.4, @0.3, @0.2, @0.1, @0];
        opacityAnimation.keyTimes = @[@0, @0.1, @0.2, @0.3, @0.4, @0.5, @0.6, @0.7, @0.8, @0.9, @1];
        
        animationGroup.animations = @[scaleAnimation, opacityAnimation];
        [pulsingLayer addAnimation:animationGroup forKey:@"plulsing"];
        [ret addObject:pulsingLayer];
    }
    return ret;
}

@end
