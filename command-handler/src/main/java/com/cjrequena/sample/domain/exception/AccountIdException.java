package com.cjrequena.sample.domain.exception;

public class AccountIdException extends DomainRuntimeException {
  public AccountIdException(String message) {
    super(message);
  }
}
