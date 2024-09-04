package net.duchung.quora.repository;

import net.duchung.quora.data.entity.FollowUser;
import net.duchung.quora.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<FollowUser, Long> {

    Optional<FollowUser> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query(value = "SELECT u.* FROM users u JOIN follows f ON u.id = f.following_id WHERE f.follower_id = :followerId",nativeQuery = true)
    List<User> getUserByFollowerId(Long followerId);

    @Query(value = "SELECT u.* FROM users u JOIN follows f ON u.id = f.follower_id WHERE f.following_id = :followingId",nativeQuery = true)
    List<User> getUserByFollowingId(Long followingId);

}
