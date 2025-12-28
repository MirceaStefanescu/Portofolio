package com.portfolio.pipeline.flink;

final class PredictionScorer {
  private PredictionScorer() {}

  static double score(double avgValue, long eventCount) {
    // Simple heuristic as a placeholder for a real ML model.
    return avgValue * 1.2 + (eventCount * 0.05);
  }
}
