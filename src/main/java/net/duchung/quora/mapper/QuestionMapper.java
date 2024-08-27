package net.duchung.quora.mapper;

import net.duchung.quora.dto.QuestionDto;
import net.duchung.quora.dto.TopicDto;
import net.duchung.quora.entity.Question;
import net.duchung.quora.entity.Topic;
import net.duchung.quora.repository.TopicRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface QuestionMapper {


    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);


    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "user.avatarUrl", target = "userAvatarUrl")
    @Mapping(source = "topics", target = "topicIds")
    QuestionDto toQuestionDto(Question question);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "userFullName", target = "user.fullName")
    @Mapping(source = "userAvatarUrl", target = "user.avatarUrl")
    Question toQuestion(QuestionDto questionDto);


    Set<TopicDto> toTopicDtos(Set<Topic> topics);

    // Custom mapping method
    default Set<Long> toTopicIds(Set<Topic> topics) {
        if (topics==null) return new HashSet<>();
        return topics.stream()
                .map(Topic::getId) // Assuming Topic has getId method
                .collect(Collectors.toSet());
    }

    // Custom mapping method to convert Set<Long> to Set<Topic>



}
