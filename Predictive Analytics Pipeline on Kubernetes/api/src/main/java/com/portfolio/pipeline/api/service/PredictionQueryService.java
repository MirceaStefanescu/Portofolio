package com.portfolio.pipeline.api.service;

import com.portfolio.pipeline.api.model.PredictionResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PredictionQueryService {
  private final JdbcTemplate jdbcTemplate;

  public PredictionQueryService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<PredictionResponse> fetchPredictions(String eventType, int limit) {
    int safeLimit = Math.min(Math.max(limit, 1), 200);
    StringBuilder sql = new StringBuilder(
        "SELECT event_type, window_start, window_end, event_count, avg_value, prediction_score FROM predictions"
    );
    List<Object> params = new ArrayList<>();

    if (eventType != null && !eventType.isBlank()) {
      sql.append(" WHERE event_type = ?");
      params.add(eventType);
    }

    sql.append(" ORDER BY window_end DESC LIMIT ?");
    params.add(safeLimit);

    return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) ->
        new PredictionResponse(
            rs.getString("event_type"),
            rs.getTimestamp("window_start").toInstant(),
            rs.getTimestamp("window_end").toInstant(),
            rs.getLong("event_count"),
            rs.getDouble("avg_value"),
            rs.getDouble("prediction_score")
        )
    );
  }
}
