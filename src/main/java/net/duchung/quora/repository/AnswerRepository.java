package net.duchung.quora.repository;
import net.duchung.quora.common.utils.Constant;
import net.duchung.quora.data.entity.Answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a" +
//            "JOIN Question q ON a.question.id = q.id" +
            " WHERE" +
            " lower(a.content) LIKE  lower(concat('%',:content,'%'))" +
            " OR lower(a.question.title) LIKE  lower(concat('%',:content,'%'))" +
            " AND (:topic is null OR  :topic in (SELECT t.name FROM Topic t))")
    Page<Answer> searchByContent(@Param("content") String keyword,@Param("topic") String topic, Pageable pageable);

    @Query("SELECT c.answer FROM Comment c WHERE c.id = :commentId")
    Optional<Answer> findByCommentId(Long commentId);

    @Query(value = "SELECT a.* FROM answers a where a.user_id = :userId",nativeQuery = true)
    List<Answer> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Answer a WHERE a.id = :answerId AND a.user.id = :userId")
    Boolean isUpVotedByUser(@Param("answerId") Long answerId, @Param("userId") Long userId);
    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId")
    List<Answer> findByQuestionId(@Param("questionId") Long questionId);

    @Query(value = "SELECT a.id , " +
            "a.viral_points/POW(1.6, COALESCE(v.view_count, 0))/POW(1.001,TIMESTAMPDIFF(MINUTE, a.created_at, NOW())*1.0)*"+ Constant.VIRAL_ANSWER_WEIGHT*Constant.BALANCE_ARGUMENT +" as order_value " +
            "FROM answers a " +
            "JOIN questions q ON a.question_id = q.id " +
            "JOIN question_topic qt ON q.id = qt.question_id " +
            "JOIN topics t ON qt.topic_id = t.id " +
            "JOIN user_topic ut ON ut.topic_id = t.id " +
            "left JOIN views v ON v.answer_id =a.id AND ut.user_id=v.user_id " +
            "WHERE ut.user_id = :userId "  ,nativeQuery = true)
    List<Object[]> recommendationByViralAnswer(@Param("userId") Long userId);
    @Query(value = "SELECT a.id," +
            "a.viral_points/POW(1.6, COALESCE(v.view_count, 0))/POW(1.001,TIMESTAMPDIFF(MINUTE, a.created_at, NOW())*1.0)*"+Constant.FOLLOWING_USER_FEED_WEIGHT*Constant.BALANCE_ARGUMENT+" as order_value " +            "FROM answers a " +
            "JOIN questions q ON a.question_id = q.id " +
            "JOIN question_topic qt ON q.id = qt.question_id " +
            "JOIN topics t ON qt.topic_id = t.id " +
            "JOIN user_topic ut ON ut.topic_id = t.id " +
            "LEFT JOIN views v ON v.answer_id =a.id AND ut.user_id=v.user_id " +
            "WHERE ut.user_id in ( " +
            "        select following_id from follows " +
            "        where follower_id = :userId " +
            "  ) "  ,nativeQuery = true)
    List<Object[]> recommendationByFollowingUserFeed(@Param("userId") Long userId);

    @Query(value = "SELECT a.id ," +
            "a.viral_points/POW(1.6, COALESCE(v.view_count, 0))/POW(1.001,TIMESTAMPDIFF(MINUTE, a.created_at, NOW())*1.0) *"+Constant.FOLLOWING_USER_ANSWER_WEIGHT*Constant.BALANCE_ARGUMENT+" as order_value " +            "FROM answers a " +
            "JOIN users u on  u.id = a.user_id " +
            "LEFT JOIN views v on u.id=v.user_id and v.answer_id=a.id " +
            "WHERE a.user_id in ( " +
            "        select id from users " +
            "        where users.id = :userId " +
            ") "  ,nativeQuery = true)
    List<Object[]> recommendationByFollowingUserAnswers(@Param("userId") Long userId);

    @Query(value = "SELECT a.id ," +
            "IF(TIMESTAMPDIFF(MINUTE, a.created_at, NOW()) < 5, 0," +
            "a.viral_points/POW(1.6, COALESCE(v.view_count, 0))/POW(1.001,TIMESTAMPDIFF(MINUTE, a.created_at, NOW())*1.0))*"+Constant.FOLLOWING_QUESTION_WEIGHT*Constant.BALANCE_ARGUMENT+" as order_value " +
            "FROM answers a " +
            "JOIN  follow_question fl on fl.question_id = a.question_id " +
            "LEFT JOIN views v ON v.user_id = fl.follower_id and v.answer_id = a.id " +
            "WHERE fl.follower_id = :userId " ,nativeQuery = true)
    List<Object[]> recommendationByFollowQuestions(@Param("userId") Long userId);
    @Query(value = "SELECT a.id ," +
            "a.viral_points/POW(1.6, COALESCE(v.view_count, 0))/POW(1.001,TIMESTAMPDIFF(MINUTE, a.created_at, NOW())*1.0)* "+Constant.FOLLOWING_USER_QUESTION_WEIGHT*Constant.BALANCE_ARGUMENT+" as order_value " +
            "FROM answers a " +
            "JOIN users u on  u.id = a.user_id " +
            "JOIN questions q on q.id = a.question_id " +
            "LEFT JOIN views v ON v.user_id = u.id and v.answer_id = a.id " +
            "WHERE q.user_id in ( " +
            "        select id from users " +
            "        where users.id = :userId " +
            ") "  ,nativeQuery = true)
    List<Object[]> recommendationByFollowingUserQuestions(@Param("userId") Long userId);

    @Query(value = "select a.id " +
            "from answers a " +
            "JOIN questions on questions.id = a.question_id " +
            "JOIN question_topic on questions.id = question_topic.question_id " +
            "JOIN user_topic on user_topic.topic_id = question_topic.topic_id " +
            "JOIN users on user_topic.user_id=users.id " +
            "WHERE users.id = :userId " +
            "ORDER BY TIMESTAMPDIFF(SECOND, a.created_at, NOW()) ",nativeQuery = true)
    List<Object> recommendationByRecentAnswerInTopic(@Param("userId") Long userId);
    @Query(value = "SELECT a.id, " +
            "a.viral_points/POW(1.6, COALESCE(v.view_count, 0))/POW(1.001,TIMESTAMPDIFF(MINUTE, a.created_at, NOW())*1.0) as order_value " +            "FROM answers a " +
            "JOIN views v ON v.answer_id = a.id AND v.user_id = :userId "
              ,nativeQuery = true)
    List<Object[]> recommendationByViralAnswerAllTopic(@Param("userId") Long userId);


}


