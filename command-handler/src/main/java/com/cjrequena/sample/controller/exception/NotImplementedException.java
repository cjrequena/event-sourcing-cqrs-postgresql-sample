package com.cjrequena.sample.controller.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class NotImplementedException extends ControllerRuntimeException {
  public NotImplementedException() {
    super(HttpStatus.NOT_IMPLEMENTED);
  }

  public NotImplementedException(String message) {
    super(HttpStatus.NOT_IMPLEMENTED, message);
  }
}
