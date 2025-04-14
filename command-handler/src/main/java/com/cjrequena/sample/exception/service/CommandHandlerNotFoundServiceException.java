package com.cjrequena.sample.exception.service;

public class CommandHandlerNotFoundServiceException extends RuntimeServiceException {
  public CommandHandlerNotFoundServiceException(String message) {
    super(message);
  }
}
