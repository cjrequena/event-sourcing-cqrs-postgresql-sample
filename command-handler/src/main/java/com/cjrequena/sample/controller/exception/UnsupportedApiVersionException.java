package com.cjrequena.sample.controller.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class UnsupportedApiVersionException extends ControllerRuntimeException {
  public UnsupportedApiVersionException() {
    super(HttpStatus.NOT_ACCEPTABLE);
  }

  public UnsupportedApiVersionException(String message) {
    super(HttpStatus.NOT_ACCEPTABLE, message);
  }

  public UnsupportedApiVersionException(String message, Throwable throwable) {
    super(HttpStatus.NOT_ACCEPTABLE, message, throwable);
  }
}
