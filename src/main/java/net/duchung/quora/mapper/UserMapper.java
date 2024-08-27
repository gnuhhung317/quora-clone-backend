package net.duchung.quora.mapper;

import net.duchung.quora.dto.TopicDto;
import net.duchung.quora.dto.UserDto;
import net.duchung.quora.entity.Topic;
import net.duchung.quora.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "topics", source = "topics")
    UserDto toUserDto(User user);

    @Mapping(target = "topics", source = "topics")
    User toUser(UserDto userDto);

    // Ánh xạ giữa Set<Topic> và Set<TopicDto>
    Set<TopicDto> toTopicDtoSet(Set<Topic> topics);

    Set<Topic> toTopicSet(Set<TopicDto> topicDtos);
}
