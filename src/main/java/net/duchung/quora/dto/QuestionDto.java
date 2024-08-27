package net.duchung.quora.dto;

import lombok.*;
import net.duchung.quora.entity.BaseEntity;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionDto extends BaseDto {

    private String title;
    private Long userId;
    private String userFullName;

    private String userAvatarUrl;

    private Set<Long> topicIds;


}
