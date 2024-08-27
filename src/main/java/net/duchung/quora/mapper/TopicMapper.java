package net.duchung.quora.mapper;

import net.duchung.quora.dto.TopicDto;
import net.duchung.quora.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

     TopicDto toTopicDto(Topic topic) ;
}
