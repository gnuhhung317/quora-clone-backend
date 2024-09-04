package net.duchung.quora.service.impl;

import net.duchung.quora.data.entity.Topic;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.service.TopicService;
import net.duchung.quora.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Override
    public List<Topic> getAllTopics(int page) {
        PageRequest pageRequest = PageRequest.of(page, Utils.SEARCH_RESULTS_PER_PAGE, Sort.by("viralPoints").descending());
        return topicRepository.findAll(pageRequest).getContent();
    }
}
