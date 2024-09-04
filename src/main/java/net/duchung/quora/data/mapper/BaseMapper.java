package net.duchung.quora.data.mapper;

import net.duchung.quora.data.dto.BaseDto;
import net.duchung.quora.data.entity.BaseEntity;

public class BaseMapper {
    public static void getBaseDtoAttribute(BaseDto baseDto, BaseEntity baseEntity) {
        baseDto.setId(baseEntity.getId());
        baseDto.setCreatedAt(baseEntity.getCreatedAt());
        baseDto.setUpdatedAt(baseEntity.getUpdatedAt());
    }

    public static void getBaseEntityAttribute(BaseEntity baseEntity,BaseDto baseDto) {
        baseEntity.setCreatedAt(baseDto.getCreatedAt());
        baseEntity.setUpdatedAt(baseDto.getUpdatedAt());
    }
}
