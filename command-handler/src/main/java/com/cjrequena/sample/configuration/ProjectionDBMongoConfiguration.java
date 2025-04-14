package com.cjrequena.sample.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
  basePackages = {"com.cjrequena.sample.repository.mongo"}
)
public class ProjectionDBMongoConfiguration {
}
