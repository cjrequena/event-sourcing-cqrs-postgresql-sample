package com.cjrequena.eventstore.sample.exception.service;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class EventStoreAggregateNotFoundServiceException extends ServiceException {
  public EventStoreAggregateNotFoundServiceException(String message) {
    super(message);
  }
}
