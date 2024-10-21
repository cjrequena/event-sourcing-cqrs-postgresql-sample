package com.cjrequena.sample.configuration;

import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Validated
@Configuration
@ConfigurationProperties(prefix = "eventstore")
public class EventStoreConfigurationProperties {

  private static final SnapshotProperties NO_SNAPSHOT = new SnapshotProperties(false, 0);

  @Valid
  @NestedConfigurationProperty
  @Setter
  private Map<String, SnapshotProperties> snapshot = new HashMap<>();

  public SnapshotProperties getSnapshot(String aggregateType) {
    return snapshot.getOrDefault(aggregateType, NO_SNAPSHOT);
  }

  public record SnapshotProperties(boolean enabled, int interval) {
  }
}
