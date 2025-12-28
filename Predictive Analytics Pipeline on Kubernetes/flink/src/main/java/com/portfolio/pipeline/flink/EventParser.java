package com.portfolio.pipeline.flink;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class EventParser {
  private static final Logger LOG = LoggerFactory.getLogger(EventParser.class);
  private static final ObjectMapper MAPPER = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  private EventParser() {}

  static Event parse(String payload) {
    if (payload == null || payload.isBlank()) {
      return null;
    }
    try {
      Event event = MAPPER.readValue(payload, Event.class);
      if (event.timestamp == 0L) {
        event.timestamp = System.currentTimeMillis();
      }
      if (event.eventType == null || event.eventType.isBlank()) {
        return null;
      }
      return event;
    } catch (Exception e) {
      LOG.warn("Failed to parse event payload", e);
      return null;
    }
  }
}
