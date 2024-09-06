package net.duchung.quora.repository.elastic;

import net.duchung.quora.data.document.FollowDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsUserFollowRepository extends ElasticsearchRepository<FollowDocument, Long> {

}
