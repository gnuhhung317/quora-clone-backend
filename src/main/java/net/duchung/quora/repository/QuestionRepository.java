package net.duchung.quora.repository;
import net.duchung.quora.entity.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q WHERE q.user.id = :userId ORDER BY q.createdAt DESC")
    List<Question> findByUserId(Long userId);
}
