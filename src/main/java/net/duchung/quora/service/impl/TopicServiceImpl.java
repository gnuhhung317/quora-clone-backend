package net.duchung.quora.service.impl;

import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.data.entity.Topic;
import net.duchung.quora.data.response.TopicResponse;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.service.TopicService;
import net.duchung.quora.common.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Override
    public List<TopicResponse> getAllTopics() {

        List<Topic> topics = topicRepository.findAll();
        return topics.stream().map(tp ->new TopicResponse(tp,countFollowers(tp.getId()),countQuestions(
                tp.getId()
        ))).toList();
    }

    @Override
    public TopicResponse getTopicById(Long id) {

        Topic topic = topicRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Topic with id "+id+" not found"));
        return new TopicResponse(topic,countFollowers(topic.getId()),countQuestions(topic.getId()));
    }

    @Override
    public Integer countFollowers(Long id) {
        return topicRepository.countFollowers(id);
    }
    @Override
    public Integer countQuestions(Long id) {
        return topicRepository.countQuestions(id);
    }
}
