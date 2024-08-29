package net.duchung.quora.service;

import net.duchung.quora.dto.ViewDto;
import net.duchung.quora.entity.View;
import org.springframework.stereotype.Service;

@Service
public interface ViewService {

    void logView(ViewDto viewDto);
}
