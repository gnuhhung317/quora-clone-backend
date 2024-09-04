package net.duchung.quora.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.data.mapper.AnswerMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile extends UserResponse {
    private Long questionCount;
    private Long answerCount;
    private List<AnswerResponse> answers;
    private List<QuestionResponse> questions;

    public UserProfile(User user) {
        super(user);
        answers =new ArrayList<>();
        questions=new ArrayList<>();
        if(user.getAnswers()!=null){
            answerCount = (long) user.getAnswers().size();
            answers.addAll(user.getAnswers().stream().map(answer -> AnswerMapper.toAnswerResponse(answer,user.getId())).toList());

        }else {
            answerCount = 0L;
        }

        if(user.getQuestions()!=null){
            questionCount = (long) user.getQuestions().size();
            questions.addAll(user.getQuestions().stream().map(question -> new QuestionResponse(question,user.getId())).toList());

        }else {
            questionCount = 0L;
        }

    }
}
