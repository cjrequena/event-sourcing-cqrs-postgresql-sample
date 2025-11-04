package com.cjrequena.sample.domain.exception;

public class CommandHandlerNotFoundException extends DomainRuntimeException {
  public CommandHandlerNotFoundException(String message) {
    super(message);
  }
}
