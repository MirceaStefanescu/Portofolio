package com.portfolio.pipeline.flink;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EventAggregateTest {
  @Test
  void aggregatesCountAndSum() {
    EventAggregate aggregate = new EventAggregate();
    EventAccumulator acc = aggregate.createAccumulator();

    aggregate.add(new Event("sensor.temperature", 10.0, 1L), acc);
    aggregate.add(new Event("sensor.temperature", 20.0, 2L), acc);

    EventAccumulator result = aggregate.getResult(acc);
    assertEquals(2, result.count);
    assertEquals(30.0, result.sum, 0.0001);
  }

  @Test
  void mergesAccumulators() {
    EventAggregate aggregate = new EventAggregate();

    EventAccumulator left = aggregate.createAccumulator();
    left.count = 3;
    left.sum = 15.0;

    EventAccumulator right = aggregate.createAccumulator();
    right.count = 2;
    right.sum = 9.0;

    EventAccumulator merged = aggregate.merge(left, right);
    assertEquals(5, merged.count);
    assertEquals(24.0, merged.sum, 0.0001);
  }
}
