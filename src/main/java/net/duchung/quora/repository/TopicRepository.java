package net.duchung.quora.repository;
import net.duchung.quora.data.entity.Topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findAll(Pageable pageable);
}
