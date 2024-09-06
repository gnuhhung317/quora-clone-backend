//package net.duchung.quora.data.document;
//
//import jakarta.persistence.Id;
//import lombok.Data;
//import net.duchung.quora.data.response.UserResponse;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import java.util.Set;
//
//@Data
//@Document(indexName = "question")
//public class QuestionDocument {
//    @Id
//    private Long id;
//    @Field(type = FieldType.Text)
//    private String title;
//    @Field(type = FieldType.Object)
//    private UserResponse author;
//    @Field(type = FieldType.Long)
//    private Long followCount;
//    @Field(type = FieldType.Long)
//    private Long answerCount;
//    @Field(type = FieldType.Boolean)
//    private boolean isFollowed;
//    @Field(type = FieldType.Long)
//    private Set<Long> topicIds;
//
//}
