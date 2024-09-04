package net.duchung.quora.data.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentRequest {

    private String content;
    private Long answerId;
    private Long parentId;
}
