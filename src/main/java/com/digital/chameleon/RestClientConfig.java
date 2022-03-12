package com.digital.chameleon;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

  @Autowired
  private Environment env;

  @Override
  @Bean
  public RestHighLevelClient elasticsearchClient() {

    String hostAndPort = env.getProperty("spring.elasticsearch.rest.uris");

    final ClientConfiguration clientConfiguration =
        ClientConfiguration.builder().connectedTo(hostAndPort).build();

    return RestClients.create(clientConfiguration).rest();
  }
}
