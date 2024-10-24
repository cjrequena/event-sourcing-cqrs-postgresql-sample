package com.cjrequena.sample.exception.service;

import com.cjrequena.eventstore.sample.exception.service.RuntimeServiceException;

public class AmountServiceException extends RuntimeServiceException {
  public AmountServiceException(String message) {
    super(message);
  }
}
