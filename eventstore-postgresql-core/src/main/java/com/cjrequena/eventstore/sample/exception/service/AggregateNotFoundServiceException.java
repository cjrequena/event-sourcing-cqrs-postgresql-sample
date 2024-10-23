package com.cjrequena.eventstore.sample.exception.service;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class AggregateNotFoundServiceException extends ServiceException {
  public AggregateNotFoundServiceException(String message) {
    super(message);
  }
}
