package net.duchung.quora.service.impl;

import jdk.jshell.execution.Util;
import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.mapper.AnswerMapper;
import net.duchung.quora.mapper.BaseMapper;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.service.RecommendationService;
import net.duchung.quora.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private AnswerRepository answerRepository;
    @Override
    public List<AnswerDto> getRecommendationAnswers(Long userId) {
        List<Answer> answers = new ArrayList<>(answerRepository.recommendationByViralAnswer(userId));
        if(answers.size()< Utils.MAX_RECOMMENDATION){
            answers.addAll(answerRepository.recommendationByFollowingUserQuestions(userId));
        }
        if(answers.size()< Utils.MAX_RECOMMENDATION){
            answers.addAll(answerRepository.recommendationByFollowingUserAnswers(userId));
        }
        if(answers.size()< Utils.MAX_RECOMMENDATION){
            answers.addAll(answerRepository.recommendationByRecentAnswerInTopic(userId));
        }
        if(answers.size()< Utils.MAX_RECOMMENDATION){
            answers.addAll(answerRepository.recommendationByRecentAnswerInTopic(userId));
        }

        return answers.stream().map(this::toAnswerDto).collect(Collectors.toList());
    }
    public AnswerDto toAnswerDto(Answer answer) {
        AnswerDto answerDto = AnswerMapper.INSTANCE.toAnswerDTO(answer);
        BaseMapper.getBaseDtoAttribute(answerDto,answer);
        return answerDto;
    }
}
