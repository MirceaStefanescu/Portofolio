package com.portfolio.pipeline.api.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
  @Bean(destroyMethod = "close")
  public RestClient restClient(@Value("${app.elasticsearch.host}") String host) {
    return RestClient.builder(HttpHost.create(host)).build();
  }
}
