package net.duchung.quora.repository.elastic;

import net.duchung.quora.data.document.AnswerDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsAnswerRepository extends ElasticsearchRepository<AnswerDocument,Long> {
//    List<AnswerDocument> findByBody(String body);
}
