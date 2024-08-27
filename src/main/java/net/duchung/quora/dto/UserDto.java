package net.duchung.quora.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto extends BaseDto {

    private String fullName;

    private String email;

    private String password;

    private Set<TopicDto> topics;



}
