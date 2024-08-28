package net.duchung.quora.repository;
import net.duchung.quora.entity.Answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a WHERE" +
            " lower(a.content) LIKE  lower(concat('%',:content,'%'))" +
            " OR lower(a.question.title) LIKE  lower(concat('%',:content,'%'))" +
            " AND (:topic is null OR lower(a.question.topic.name) LIKE  lower(concat('%',:topic,'%')))")
    Page<Answer> searchByContent(@Param("content") String content,@Param("topic") String topic, Pageable pageable);

    @Query("SELECT c.answer FROM Comment c WHERE c.id = :commentId")
    Optional<Answer> findByCommentId(Long commentId);
}
