package net.duchung.quora.service.impl;

import lombok.RequiredArgsConstructor;
import net.duchung.quora.data.dto.InteractionRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionDataServiceImpl {
    private final JdbcTemplate jdbcTemplate;

    public List<InteractionRecord> getInteractionData() {
        String sql = "SELECT user_id, answer_id, total_interaction FROM quora.answer_interaction_view";
        return jdbcTemplate.query(sql, interactionRowMapper);
    }

    // Map each row to a custom DTO
    private RowMapper<InteractionRecord> interactionRowMapper = (rs, rowNum) -> {
        InteractionRecord record = new InteractionRecord();
        record.setUser_id(rs.getInt("user_id"));
        record.setAnswer_id(rs.getInt("answer_id"));
        record.setTotal_interaction(rs.getFloat("total_interaction"));
        return record;
    };
}
