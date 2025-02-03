package net.duchung.quora.service;

import net.duchung.quora.data.dto.InteractionRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InteractionDataService {
    List<InteractionRecord> getInteractionData();
}
