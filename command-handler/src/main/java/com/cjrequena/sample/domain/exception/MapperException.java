package com.cjrequena.sample.domain.exception;

public class MapperException extends DomainRuntimeException {

  public MapperException(Throwable ex) {
    super(ex);
  }

  public MapperException(String message) {
    super(message);
  }

  public MapperException(String message, Throwable ex) {
    super(message, ex);
  }
}
