package net.duchung.quora.mapper;

import net.duchung.quora.dto.CommentDto;
import net.duchung.quora.entity.Comment;
import net.duchung.quora.entity.vote.AnswerVote;
import net.duchung.quora.entity.vote.CommentVote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Set;

@Mapper
public interface CommentMapper {

    @Mapping(source = "answer.id", target = "answerId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "user.avatarUrl", target = "userAvatarUrl")
    @Mapping(target = "upvotes", expression = "java(countUpvotes(comment.getVotes()))")
    @Mapping(target = "downvotes", expression = "java(countDownvotes(comment.getVotes()))")
    @Mapping(source = "parentComment.id", target = "parentId")
    CommentDto toCommentDto(Comment comment);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "userFullName", target = "user.fullName")
    @Mapping(source = "userAvatarUrl", target = "user.avatarUrl")
    @Mapping(source = "answerId", target = "answer.id")
    @Mapping(source = "parentId", target = "parentComment.id")
    Comment toComment(CommentDto commentDto);

    // Phương thức tùy chỉnh để đếm upvotes
    default Long countUpvotes(Set<CommentVote> votes) {
        if (votes == null) return 0L;
        return votes.stream().filter(CommentVote::getIsUpvote).count();
    }

    // Phương thức tùy chỉnh để đếm downvotes
    default Long countDownvotes(Set<CommentVote> votes) {
        if (votes == null) return 0L;
        return votes.stream().filter(vote -> !vote.getIsUpvote()).count();
    }
}



