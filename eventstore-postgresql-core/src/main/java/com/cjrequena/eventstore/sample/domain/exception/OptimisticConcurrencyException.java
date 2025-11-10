package com.cjrequena.eventstore.sample.domain.exception;

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
}
