package com.cjrequena.sample.exception.service;

import com.cjrequena.eventstore.sample.exception.service.ServiceException;

public class IllegalArgumentServiceException extends ServiceException {
  public IllegalArgumentServiceException(String message) {
    super(message);
  }
}
