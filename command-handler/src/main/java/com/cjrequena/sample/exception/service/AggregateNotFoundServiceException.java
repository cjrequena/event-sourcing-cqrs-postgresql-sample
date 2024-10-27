package com.cjrequena.sample.exception.service;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class AggregateNotFoundServiceException extends RuntimeException {
  public AggregateNotFoundServiceException(String message) {
    super(message);
  }
}
