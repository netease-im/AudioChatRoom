//
//  NTESAudioPlayerManager.m
//  NIMAudioChatroom
//
//  Created by Netease on 2019/3/11.
//  Copyright © 2019年 netease. All rights reserved.
//

#import "NTESAudioPlayerManager.h"

@interface NTESAudioPlayerManager ()<NIMNetCallManagerDelegate, NTESAudioPlayerDelegate>

@property (nonatomic, strong) NSMutableArray <NIMNetCallAudioFileMixTask *> *songs;
@property (nonatomic, assign) NSInteger index;
@property (nonatomic, strong) NTESAudioPlayerView *playerView;
@property (nonatomic, assign) BOOL isPause;
@end

@implementation NTESAudioPlayerManager

- (instancetype)init {
    if (self = [super init]) {
        
        [[NIMAVChatSDK sharedSDK].netCallManager addDelegate:self];
        
        _songs = [NSMutableArray array];
        for (int i = 0; i < 2; i++) {
            NSString *name = [NSString stringWithFormat:@"%d", i+1];
            NSString *path = [[NSBundle mainBundle] pathForResource:name ofType:@"mp3"];
            if (path) {
                NSURL *url = [NSURL fileURLWithPath:path];
                NIMNetCallAudioFileMixTask *task = [[NIMNetCallAudioFileMixTask alloc] initWithFileURL:url];
                [_songs addObject:task];
            }
        }
        
        self.playerView.musicName = @"音乐0";
        self.playerView.playState = NO;
    }
    return self;
}

- (void)stop {
    _index = 0;
    [[NIMAVChatSDK sharedSDK].netCallManager stopAudioMix];
    [[NIMAVChatSDK sharedSDK].netCallManager removeDelegate:self];
    self.playerView.playState = NO;
}

- (void)start {
    if (_songs.count == 0) {
        return;
    }
    _index = _index % _songs.count;
    NIMNetCallAudioFileMixTask *task = _songs[_index++];
    [[NIMAVChatSDK sharedSDK].netCallManager startAudioMix:task];
    self.playerView.musicName = [NSString stringWithFormat:@"音乐%d", (int)_index];
    if (_playerView.playState == NO) {
        [[NIMAVChatSDK sharedSDK].netCallManager pauseAudioMix];
    }
}

#pragma mark - <NTESAudioPlayerDelegate>
- (void)didStartPlayAction:(BOOL)isPause {
    if (isPause) {
        _playerView.playState = NO;
        _isPause = YES;
        [[NIMAVChatSDK sharedSDK].netCallManager pauseAudioMix];
    } else {
        _playerView.playState = YES;
        if (!_isPause) {
            [self start];
        } else {
            [[NIMAVChatSDK sharedSDK].netCallManager resumeAudioMix];
        }
        _isPause = NO;
    }
}

- (void)didNextAction {
    [self start];
}

#pragma mark - <NIMNetCallManagerDelegate>
- (void)onAudioMixTaskCompleted {
    [self didNextAction];
}

#pragma mark - Getter
- (NTESAudioPlayerView *)playerView {
    if (!_playerView) {
        _playerView = [[NTESAudioPlayerView alloc] init];
        _playerView.delegate =self;
    }
    return _playerView;
}

- (NTESAudioPlayerView *)view {
    return self.playerView;
}

@end
