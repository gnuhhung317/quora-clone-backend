package net.duchung.quora.repository.elastic;

import net.duchung.quora.data.document.AnswerDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsAnswerRepository extends ElasticsearchRepository<AnswerDocument,Long> {
//    @Query("""
//            {
//                          "query": {
//                            "multi_match": {
//                              "query": "comment",
//                              "fields": ["content", "questionContent" ],
//                              "fuzziness": "AUTO"
//                            }
//                          }
//                        }
//            """)
    List<AnswerDocument> findByContentOrQuestionContent(String content,String questionContent);

    @Query(value = """
        {
          "bool":{
            "must":[
              {
                "fuzzy":{
                  "name": "#{#content}"
                }
              }
            ]
          }
        }
        """,nativeQuery = true)
    List<AnswerDocument> findFuzzyByContent(String content);

}
