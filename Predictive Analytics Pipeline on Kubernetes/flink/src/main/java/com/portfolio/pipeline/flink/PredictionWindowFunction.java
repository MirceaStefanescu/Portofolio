package com.portfolio.pipeline.flink;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PredictionWindowFunction
    extends ProcessWindowFunction<EventAccumulator, Prediction, String, TimeWindow> {
  private static final Logger LOG = LoggerFactory.getLogger(PredictionWindowFunction.class);

  @Override
  public void process(
      String key,
      Context context,
      Iterable<EventAccumulator> aggregates,
      Collector<Prediction> out
  ) {
    EventAccumulator acc = aggregates.iterator().next();
    double avg = acc.count == 0 ? 0.0 : acc.sum / acc.count;
    double score = PredictionScorer.score(avg, acc.count);
    Prediction prediction = new Prediction(
        key,
        context.window().getStart(),
        context.window().getEnd(),
        acc.count,
        avg,
        score,
        System.currentTimeMillis()
    );
    LOG.info("prediction eventType={} count={} avg={} score={}", key, acc.count, avg, score);
    out.collect(prediction);
  }
}
