package com.cjrequena.eventstore.sample.domain.exception;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class AggregateNotFoundException extends DomainException {
  public AggregateNotFoundException(String message) {
    super(message);
  }
}
