package net.duchung.quora.mapper;

import net.duchung.quora.dto.BaseDto;
import net.duchung.quora.entity.BaseEntity;

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
