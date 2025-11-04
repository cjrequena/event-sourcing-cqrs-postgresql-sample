package com.cjrequena.sample.domain.exception;

public class AccountBalanceException extends DomainRuntimeException {
  public AccountBalanceException(String message) {
    super(message);
  }
}
