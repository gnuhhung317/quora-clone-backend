package net.duchung.quora.service;

import net.duchung.quora.data.dto.ViewDto;
import org.springframework.stereotype.Service;

@Service
public interface ViewService {

    void logView(ViewDto viewDto);
}
