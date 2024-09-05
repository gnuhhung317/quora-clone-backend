package net.duchung.quora.repository;
import net.duchung.quora.data.entity.Question;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q WHERE q.user.id = :userId ORDER BY q.createdAt DESC")
    List<Question> findByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT q.* FROM questions q" +
            " JOIN question_topic qt ON q.id = qt.question_id" +
            " JOIN topics t ON qt.topic_id = t.id" +
            " JOIN user_topic ut ON ut.topic_id = t.id" +
            " WHERE ut.user_id = :userId" +
            " Order BY RAND() LIMIT 5",nativeQuery = true  )
    List<Question> findBySuggestQuestion(Long userId);
}
