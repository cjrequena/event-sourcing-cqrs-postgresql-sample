package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.entity.EventEntity;
import jakarta.annotation.Nonnull;

public interface AsyncEventHandler {

  void handle(EventEntity eventEntity);

  @Nonnull
  String getAggregateType();

}
