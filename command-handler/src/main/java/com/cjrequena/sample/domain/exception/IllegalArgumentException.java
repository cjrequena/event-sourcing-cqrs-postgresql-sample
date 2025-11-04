package com.cjrequena.sample.domain.exception;

public class IllegalArgumentException extends DomainRuntimeException {
  public IllegalArgumentException(String message) {
    super(message);
  }
}
