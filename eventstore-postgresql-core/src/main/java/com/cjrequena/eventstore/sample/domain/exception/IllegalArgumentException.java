package com.cjrequena.eventstore.sample.domain.exception;

public class IllegalArgumentException extends DomainException {
  public IllegalArgumentException(String message) {
    super(message);
  }
}
