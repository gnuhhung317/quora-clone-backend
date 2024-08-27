package net.duchung.quora.mapper;

import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.entity.vote.AnswerVote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);


    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.title", target = "questionTitle")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "user.avatarUrl", target = "userAvatarUrl")
    @Mapping(target = "upvotes", expression = "java(countUpvotes(answer.getVotes()))")
    @Mapping(target = "downvotes", expression = "java(countDownvotes(answer.getVotes()))")
    AnswerDto toAnswerDTO(Answer answer);

    @Mapping(source = "questionId", target = "question.id")
    @Mapping(source = "questionTitle", target = "question.title")
    @Mapping(source = "userFullName", target = "user.fullName")
    @Mapping(source = "userAvatarUrl", target = "user.avatarUrl")
    @Mapping(source = "userId", target = "user.id")
    Answer toAnswer(AnswerDto answerDTO);

    // Phương thức tùy chỉnh để đếm upvotes
    default Long countUpvotes(Set<AnswerVote> votes) {
        return votes.stream().filter(AnswerVote::getIsUpvote).count();
    }

    // Phương thức tùy chỉnh để đếm downvotes
    default Long countDownvotes(Set<AnswerVote> votes) {
        return votes.stream().filter(vote -> !vote.getIsUpvote()).count();
    }
}

