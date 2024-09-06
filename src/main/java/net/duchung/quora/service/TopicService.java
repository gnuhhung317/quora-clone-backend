package net.duchung.quora.service;

import net.duchung.quora.data.entity.Topic;
import net.duchung.quora.data.response.TopicResponse;
import net.duchung.quora.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopicService {

    List<TopicResponse> getAllTopics();

    TopicResponse getTopicById(Long id);
    Integer countFollowers(Long id);
    Integer countQuestions(Long id);
}
