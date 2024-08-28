package net.duchung.quora.service;

import net.duchung.quora.dto.response.ListAnswerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {

    ListAnswerResponse search(String query, String topic,int page);
}
