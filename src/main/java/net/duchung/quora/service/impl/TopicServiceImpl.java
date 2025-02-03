package net.duchung.quora.service.impl;

import lombok.RequiredArgsConstructor;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.data.entity.Topic;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.data.mapper.TopicMapper;
import net.duchung.quora.data.response.TopicResponse;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.TopicService;
import net.duchung.quora.common.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final AuthService authService;
    private final TopicMapper topicMapper = TopicMapper.INSTANCE;


    @Override
    public List<TopicResponse> getAllTopics() {

        List<Topic> topics = topicRepository.findAll();
        return topics.stream().map(tp ->new TopicResponse(tp,countFollowers(tp.getId()),countQuestions(
                tp.getId()
        ))).toList();
    }

    @Override
    public List<TopicResponse> suggestTopic() {
        User authUser = authService.getCurrentUser();
        List<Topic> topics = new ArrayList<>(authUser.getTopics().stream().toList());
        int topicsSize = topics.size();
        List<Long> topicIds = topics.stream().map(Topic::getId).toList();
        if(topicsSize <= 6) {
            topics.addAll(topicRepository.findAllByIdNotIn(topicIds, Pageable.ofSize(6 - topicsSize)));
        }
        return topics.stream().map(tp ->new TopicResponse(tp,0,0)).toList();
    }

    @Override
    public TopicResponse getTopicById(Long id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Topic with id "+id+" not found"));
        boolean isFollowed = topicRepository.existsUserIdAndTopicId(authService.getCurrentUser().getId(),id);
        TopicResponse topicResponse = new TopicResponse(topic,countFollowers(topic.getId()),countQuestions(topic.getId()));
        topicResponse.setFollowed(isFollowed);
        return topicResponse;
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
