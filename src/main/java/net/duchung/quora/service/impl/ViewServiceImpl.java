package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.dto.ViewDto;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.entity.User;
import net.duchung.quora.entity.View;
import net.duchung.quora.exception.AccessDeniedException;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.repository.ViewRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ViewServiceImpl implements ViewService {

    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Override
    @Transactional
    public void logView(ViewDto viewDto) {
        User user = authService.getCurrentUser();
        Optional<View> viewOpt = viewRepository.findByUserIdAndAnswerId(viewDto.getUserId(), viewDto.getAnswerId());
        if(viewOpt.isPresent()) {
            View view = viewOpt.get();
            if(!view.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("You don't have permission to log view this answer to other user");
            }
            view.setTotalDuration(view.getTotalDuration() + viewDto.getDuration());
            view.setViewCount(view.getViewCount() + 1);
            viewRepository.save(view);
        } else {
            View view = new View();
            Answer answer = answerRepository.findById(viewDto.getAnswerId()).orElseThrow(() -> new DataNotFoundException("Answer with id "+ viewDto.getAnswerId()+" not found"));
            view.setUser(user);
            view.setAnswer(answer);

            view.setTotalDuration(viewDto.getDuration());
            view.setViewCount(1);
            viewRepository.save(view);
        }
    }
}
