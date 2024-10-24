package com.cjrequena.sample.exception.service;

import com.cjrequena.eventstore.sample.exception.service.RuntimeServiceException;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class OptimisticConcurrencyServiceException extends RuntimeServiceException {
  public OptimisticConcurrencyServiceException(String message) {
    super(message);
  }
  public OptimisticConcurrencyServiceException(String message, Throwable ex) {
    super(message, ex);
  }
}
