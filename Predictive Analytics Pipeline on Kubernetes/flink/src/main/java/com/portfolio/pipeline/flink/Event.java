package com.portfolio.pipeline.flink;

public class Event {
  public String eventType;
  public double value;
  public long timestamp;

  public Event() {}

  public Event(String eventType, double value, long timestamp) {
    this.eventType = eventType;
    this.value = value;
    this.timestamp = timestamp;
  }
}
