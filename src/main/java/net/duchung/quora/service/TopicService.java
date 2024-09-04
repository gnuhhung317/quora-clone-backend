package net.duchung.quora.service;

import net.duchung.quora.data.entity.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopicService {

    List<Topic> getAllTopics(int page);
}
