package net.duchung.quora.mapper;

import net.duchung.quora.dto.CommentDto;
import net.duchung.quora.entity.Comment;
import net.duchung.quora.entity.vote.AnswerVote;
import net.duchung.quora.entity.vote.CommentVote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper
public interface CommentMapper {

    @Mapping(source = "answer.id", target = "answerId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "user.avatarUrl", target = "userAvatarUrl")
    @Mapping(target = "upvotes", expression = "java(countUpvotes(comment.getVotes()))")
    @Mapping(target = "downvotes", expression = "java(countDownvotes(comment.getVotes()))")
    CommentDto toCommentDto(Comment comment);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "userFullName", target = "user.fullName")
    @Mapping(source = "userAvatarUrl", target = "user.avatarUrl")

    Comment toComment(CommentDto commentDto);

    // Phương thức tùy chỉnh để đếm upvotes
    default Long countUpvotes(Set<CommentVote> votes) {
        return votes.stream().filter(CommentVote::getIsUpvote).count();
    }

    // Phương thức tùy chỉnh để đếm downvotes
    default Long countDownvotes(Set<CommentVote> votes) {
        return votes.stream().filter(vote -> !vote.getIsUpvote()).count();
    }
}



