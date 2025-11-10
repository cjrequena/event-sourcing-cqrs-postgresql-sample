package com.cjrequena.eventstore.sample.service;

import com.cjrequena.eventstore.sample.domain.model.aggregate.Aggregate;
import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.util.UUID;

/**
 * Factory for creating new instances of {@link Aggregate}.
 */
public final class AggregateFactory {

  private AggregateFactory() {
    // Prevent instantiation
  }

  /**
   * Creates a new instance of the given {@link Aggregate} class with the specified ID.
   *
   * @param aggregateClass the class type of the aggregate
   * @param aggregateId    the unique identifier for the aggregate
   * @param <T>            the type of the aggregate
   * @return a new aggregate instance
   * @throws IllegalArgumentException if instantiation fails or the constructor is missing
   */
  public static <T extends Aggregate> T newInstance(
    @NonNull Class<T> aggregateClass,
    @NonNull UUID aggregateId) {

    try {
      Constructor<T> constructor = aggregateClass.getDeclaredConstructor(UUID.class, long.class);
      constructor.setAccessible(true);
      return constructor.newInstance(aggregateId, 0L);
    } catch (ReflectiveOperationException e) {
      throw new IllegalArgumentException(
        "Failed to create instance of aggregate class: " + aggregateClass.getName(), e);
    }
  }
}
