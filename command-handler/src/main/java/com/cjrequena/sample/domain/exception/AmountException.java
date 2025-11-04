package com.cjrequena.sample.domain.exception;

public class AmountException extends DomainRuntimeException {
  public AmountException(String message) {
    super(message);
  }
}
