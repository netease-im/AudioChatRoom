package com.netease.mmc.demo.service.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.netease.mmc.demo.common.enums.LiveAVTypeEnum;
import com.netease.mmc.demo.common.enums.LiveOrientationEnums;
import com.netease.mmc.demo.common.enums.VideoTypeEnum;
import com.netease.mmc.demo.dao.domain.VoiceTouristDO;
import com.netease.mmc.demo.service.model.TouristModel;


/**
 * Model转换工具类.
 *
 * @author hzwanglin1
 * @date 17-6-26
 * @since 1.0
 */
@Mapper(imports = { VideoTypeEnum.class, LiveAVTypeEnum.class, LiveOrientationEnums.class})
public interface ModelUtil {
    ModelUtil INSTANCE = Mappers.getMapper(ModelUtil.class);

    @Mapping(source = "accid", target = "sid")
    TouristModel touristDO2Model(VoiceTouristDO touristDO);
}
