package com.cjrequena.sample.domain.exception;

import lombok.ToString;

@ToString
public abstract class DomainRuntimeException extends RuntimeException {
  public DomainRuntimeException(Throwable ex) {
    super(ex);
  }

  public DomainRuntimeException(String message) {
    super(message);
  }

  public DomainRuntimeException(String message, Throwable ex) {
    super(message, ex);
  }

}
