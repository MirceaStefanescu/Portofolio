package com.portfolio.pipeline.flink;

import org.apache.flink.api.common.functions.AggregateFunction;

class EventAggregate implements AggregateFunction<Event, EventAccumulator, EventAccumulator> {
  @Override
  public EventAccumulator createAccumulator() {
    return new EventAccumulator();
  }

  @Override
  public EventAccumulator add(Event event, EventAccumulator acc) {
    acc.count += 1;
    acc.sum += event.value;
    return acc;
  }

  @Override
  public EventAccumulator getResult(EventAccumulator acc) {
    return acc;
  }

  @Override
  public EventAccumulator merge(EventAccumulator a, EventAccumulator b) {
    EventAccumulator merged = new EventAccumulator();
    merged.count = a.count + b.count;
    merged.sum = a.sum + b.sum;
    return merged;
  }
}
