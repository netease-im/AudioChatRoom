package com.netease.mmc.demo.web.util;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.netease.mmc.demo.service.model.TouristModel;
import com.netease.mmc.demo.service.model.VoiceRoomModel;
import com.netease.mmc.demo.web.vo.TouristVO;
import com.netease.mmc.demo.web.vo.VoiceRoomCreateVO;
import com.netease.mmc.demo.web.vo.VoiceRoomListVO;


/**
 * Model转换工具类.
 *
 * @author hzwanglin1
 * @date 17-6-26
 * @since 1.0
 */
@Mapper
public interface VOUtil {
    VOUtil INSTANCE = Mappers.getMapper(VOUtil.class);

    VoiceRoomListVO roomModel2ListVO(VoiceRoomModel roomModel);

    VoiceRoomCreateVO roomModel2CreateVO(VoiceRoomModel roomModel);

    List<VoiceRoomListVO> roomModelList2VOList(List<VoiceRoomModel> modelList);

    TouristVO touristModel2VO(TouristModel model);
}
