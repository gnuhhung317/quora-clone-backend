package net.duchung.quora.service;

import net.duchung.quora.data.response.ListAnswerResponse;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {

    ListAnswerResponse search(String query, String topic,int page);
}
