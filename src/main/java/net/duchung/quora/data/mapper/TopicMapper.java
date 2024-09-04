package net.duchung.quora.data.mapper;

import net.duchung.quora.data.dto.TopicDto;
import net.duchung.quora.data.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

     TopicDto toTopicDto(Topic topic) ;
}
