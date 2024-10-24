package com.cjrequena.eventstore.sample.exception.service;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class EventStoreOptimisticConcurrencyServiceException extends RuntimeServiceException {
  public EventStoreOptimisticConcurrencyServiceException(String message) {
    super(message);
  }
}
