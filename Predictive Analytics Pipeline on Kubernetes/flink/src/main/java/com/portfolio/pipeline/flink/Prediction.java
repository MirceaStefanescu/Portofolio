package com.portfolio.pipeline.flink;

public class Prediction {
  public final String eventType;
  public final long windowStart;
  public final long windowEnd;
  public final long eventCount;
  public final double avgValue;
  public final double predictionScore;
  public final long generatedAt;

  public Prediction(
      String eventType,
      long windowStart,
      long windowEnd,
      long eventCount,
      double avgValue,
      double predictionScore,
      long generatedAt
  ) {
    this.eventType = eventType;
    this.windowStart = windowStart;
    this.windowEnd = windowEnd;
    this.eventCount = eventCount;
    this.avgValue = avgValue;
    this.predictionScore = predictionScore;
    this.generatedAt = generatedAt;
  }
}
