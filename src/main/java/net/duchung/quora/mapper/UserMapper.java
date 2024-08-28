package net.duchung.quora.mapper;

import net.duchung.quora.dto.TopicDto;
import net.duchung.quora.dto.UserDto;
import net.duchung.quora.entity.Topic;
import net.duchung.quora.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "topicIds", source = "topics")
    UserDto toUserDto(User user);

    @Mapping(target = "topics", source = "topicIds")
    User toUser(UserDto userDto);

    // Map Set<Topic> to Set<Long> (extracting topic IDs)
    default Set<Long> mapTopicsToIds(Set<Topic> topics) {
        if(topics == null) return Collections.emptySet();
        return topics.stream()
                .map(Topic::getId) // Assuming Topic has a getId() method
                .collect(Collectors.toSet());
    }

    // Map Set<Long> to Set<Topic> (creating Topic objects with IDs)
    default Set<Topic> mapIdsToTopics(Set<Long> topicIds) {
        if(topicIds == null) return Collections.emptySet();

        return topicIds.stream()
                .map(id -> {
                    Topic topic = new Topic();
                    topic.setId(id); // Assuming Topic has a setId() method
                    return topic;
                })
                .collect(Collectors.toSet());
    }

    // Map Set<Topic> to Set<TopicDto>
    Set<TopicDto> toTopicDtoSet(Set<Topic> topics);

    // Map Set<TopicDto> to Set<Topic>
    Set<Topic> toTopicSet(Set<TopicDto> topicDtos);

    // Map individual Topic to TopicDto
    TopicDto toTopicDto(Topic topic);

    // Map individual TopicDto to Topic
    Topic toTopic(TopicDto topicDto);
}
