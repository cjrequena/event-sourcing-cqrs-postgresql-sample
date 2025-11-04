package com.cjrequena.sample.domain.exception;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class OptimisticConcurrencyException extends DomainRuntimeException {
  public OptimisticConcurrencyException(String message) {
    super(message);
  }

  public OptimisticConcurrencyException(String message, Throwable ex) {
    super(message, ex);
  }
}
