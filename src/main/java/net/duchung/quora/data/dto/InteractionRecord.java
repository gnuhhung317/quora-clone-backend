package net.duchung.quora.data.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InteractionRecord {
    private int user_id;
    private int answer_id;
    private float total_interaction;

}