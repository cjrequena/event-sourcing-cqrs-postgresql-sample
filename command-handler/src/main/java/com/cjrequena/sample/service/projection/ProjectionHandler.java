package com.cjrequena.sample.service.projection;

import com.cjrequena.eventstore.sample.domain.model.aggregate.Aggregate;
import com.cjrequena.sample.domain.model.aggregate.AggregateType;
import jakarta.annotation.Nonnull;

public interface ProjectionHandler {

  void handle(Aggregate aggregate);

  @Nonnull
  AggregateType getAggregateType();
}
