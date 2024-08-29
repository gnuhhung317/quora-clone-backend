package net.duchung.quora.repository;


import net.duchung.quora.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {
    Optional<View> findByUserIdAndAnswerId(Long userId, Long questionId);

}
