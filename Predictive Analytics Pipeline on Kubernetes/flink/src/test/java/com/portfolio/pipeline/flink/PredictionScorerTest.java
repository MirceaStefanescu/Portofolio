package com.portfolio.pipeline.flink;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PredictionScorerTest {
  @Test
  void scoresPredictionUsingHeuristic() {
    double avgValue = 10.0;
    long eventCount = 5;

    double score = PredictionScorer.score(avgValue, eventCount);

    assertEquals(10.0 * 1.2 + (eventCount * 0.05), score, 0.0001);
  }
}
