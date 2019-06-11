//
//  NTESAudioPlayerManager.m
//  NIMAudioChatroom
//
//  Created by Netease on 2019/3/11.
//  Copyright © 2019年 netease. All rights reserved.
//

#import "NTESAudioPlayerManager.h"

@interface NTESAudioPlayerManager ()<NIMNetCallManagerDelegate, NTESAudioPlayerDelegate, NTESAudioPanelViewDelegate>

@property (nonatomic, strong) NSMutableArray <NIMNetCallAudioFileMixTask *> *songs;
@property (nonatomic, strong) NSMutableArray <NIMNetCallAudioFileMixTask *> *effects;
@property (nonatomic, assign) NSInteger index;
@property (nonatomic, assign) NSInteger indexOfEffect;

@property (nonatomic, strong) NTESAudioPlayerView *playerView;
@property (nonatomic, assign) BOOL isPause;
@property (nonatomic, assign) CGFloat currentPlayVolumn;
@property (nonatomic, assign) CGFloat currentEffectVolumn;
@property (nonatomic, assign) BOOL isBackground;

@end

@implementation NTESAudioPlayerManager
@synthesize audioPanelView = _audioPanelView;
- (instancetype)init {
    if (self = [super init]) {
        
        [[NIMAVChatSDK sharedSDK].netCallManager addDelegate:self];
        self.currentPlayVolumn = 0.4;
        self.currentEffectVolumn = 0.7;
        _songs = [NSMutableArray array];
        for (int i = 0; i < 2; i++) {
            NSString *name = [NSString stringWithFormat:@"%d", i+1];
            NSString *path = [[NSBundle mainBundle] pathForResource:name ofType:@"mp3"];
            if (path) {
                NSURL *url = [NSURL fileURLWithPath:path];
                NIMNetCallAudioFileMixTask *task = [[NIMNetCallAudioFileMixTask alloc] initWithFileURL:url];
                task.playbackVolume = self.currentPlayVolumn;
                [_songs addObject:task];
            }
        }
        
        _effects = [NSMutableArray array];
        for (int i = 0; i < 2; i++) {
            NSString *name = [NSString stringWithFormat:@"Synth Effects%d", i+1];
            NSString *path = [[NSBundle mainBundle] pathForResource:name ofType:@"caf"];
            if (path) {
                NSURL *url = [NSURL fileURLWithPath:path];
                NIMNetCallAudioFileMixTask *task = [[NIMNetCallAudioFileMixTask alloc] initWithFileURL:url];
                task.playbackVolume = self.currentEffectVolumn;
                [_effects addObject:task];
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
    NIMNetCallAudioFileMixTask *task = _songs[_index];
    task.playbackVolume = self.currentPlayVolumn;
    [[NIMAVChatSDK sharedSDK].netCallManager startAudioMix:task];
    self.playerView.musicName = [NSString stringWithFormat:@"音乐%d", (int)_index+1];
    if (_playerView.playState == NO) {
        [[NIMAVChatSDK sharedSDK].netCallManager pauseAudioMix];
    }
    
    [self.audioPanelView setMusicButtonSelectedAtIndex:_index];
}

- (void)playMusicAtIndex:(NSUInteger)targetIdx {
    if (_songs.count == 0 || targetIdx >= _songs.count) {
        return;
    }
    
    _index = targetIdx;
    NIMNetCallAudioFileMixTask *task = _songs[_index];
    task.playbackVolume = self.currentPlayVolumn;
    [[NIMAVChatSDK sharedSDK].netCallManager startAudioMix:task];
    self.playerView.musicName = [NSString stringWithFormat:@"音乐%d", (int)_index+1];
    self.playerView.playState = YES;
    self.isPause = NO;
    self.isBackground = YES;
}

- (void)changeMusicVolumn:(CGFloat)value {
    self.currentPlayVolumn = value;
    
    if (!self.isBackground) {
        return;
    }
    
    NIMNetCallAudioFileMixTask *task = _songs[_index];
    task.playbackVolume = self.currentPlayVolumn;
    [[NIMAVChatSDK sharedSDK].netCallManager updateAudioMix:task];
}

- (void)playEffectAtIndex:(NSUInteger)targetIdx {
    if (self.effects.count == 0 || targetIdx >= self.effects.count) {
        return;
    }
    
    self.isBackground = NO;
    self.indexOfEffect = targetIdx;
    NIMNetCallAudioFileMixTask *task = self.effects[self.indexOfEffect];
    task.playbackVolume = self.currentEffectVolumn;
    [[NIMAVChatSDK sharedSDK].netCallManager startAudioMix:task];
}

- (void)changeEffectVolumn:(CGFloat)value {
    self.currentEffectVolumn = value;
    
    if (self.isBackground) {
        return;
    }
    
    NIMNetCallAudioFileMixTask *task = self.effects[self.indexOfEffect];
    task.playbackVolume = self.currentEffectVolumn;
    [[NIMAVChatSDK sharedSDK].netCallManager updateAudioMix:task];
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
    NSInteger idx = (self.index+1)%self.songs.count;
    [self playMusicAtIndex:idx];
    
    [self.audioPanelView setMusicButtonSelectedAtIndex:idx];

}

- (void)didMoreAction {
    self.audioPanelView.hidden = NO;
}

#pragma mark - <NIMNetCallManagerDelegate>
- (void)onAudioMixTaskCompleted {
    if (!self.isBackground) {
        self.playerView.playState = NO;
        return;
    }
    [self didNextAction];
}

#pragma mark - NTESAudioPanelViewDelegate
- (void)onButtonSelected:(NTESButtonType)type {
    switch (type) {
        case NTESButtonTypeMusic1:
        {
            [self playMusicAtIndex:0];
        }
            break;
        case NTESButtonTypeMusic2:
        {
            [self playMusicAtIndex:1];
        }
            break;
        case NTESButtonTypeMusicEffect1:
        {
            [self playEffectAtIndex:0];
        }
            break;
        case NTESButtonTypeMusicEffect2:
        {
            [self playEffectAtIndex:1];
        }
            break;
        default:
            break;
    }
}
- (void)onValueChangeOfType:(NTESValueChangeType)type value:(CGFloat)value {
    switch (type) {
        case NTESValueChangeTypeMusicVolumn:
        {
            [self changeMusicVolumn:value];
        }
            break;
        case NTESValueChangeTypeMusicEffect:
        {
            [self changeEffectVolumn:value];
        }
            break;
        default:
            break;
    }
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

- (NTESAudioPanelView *)audioPanelView {
    if (!_audioPanelView) {
        _audioPanelView = [[NTESAudioPanelView alloc] init];
        _audioPanelView.delegate = self;
        _audioPanelView.hidden = YES;
    }
    return _audioPanelView;
}

@end
