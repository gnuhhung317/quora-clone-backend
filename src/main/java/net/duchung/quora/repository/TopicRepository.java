package net.duchung.quora.repository;
import net.duchung.quora.data.entity.Topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findAll(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM topics " +
            "JOIN user_topic ut ON ut.topic_id = topics.id " ,nativeQuery = true)
    Integer countFollowers(@Param("topicId") Long topicId);

    @Query(value = "SELECT COUNT(*) FROM questions " +
            "JOIN question_topic qt ON qt.question_id = questions.id AND qt.topic_id = :topicId " ,nativeQuery = true)
    Integer countQuestions(@Param("topicId") Long topicId);
}
