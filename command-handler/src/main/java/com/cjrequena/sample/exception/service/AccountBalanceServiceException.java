package com.cjrequena.sample.exception.service;

import com.cjrequena.eventstore.sample.exception.service.RuntimeServiceException;

public class AccountBalanceServiceException extends RuntimeServiceException {
  public AccountBalanceServiceException(String message) {
    super(message);
  }
}
