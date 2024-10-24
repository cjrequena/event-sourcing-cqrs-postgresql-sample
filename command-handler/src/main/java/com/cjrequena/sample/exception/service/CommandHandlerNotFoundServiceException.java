package com.cjrequena.sample.exception.service;

import com.cjrequena.eventstore.sample.exception.service.RuntimeServiceException;

public class CommandHandlerNotFoundServiceException extends RuntimeServiceException {
  public CommandHandlerNotFoundServiceException(String message) {
    super(message);
  }
}
