package com.cjrequena.sample.controller.exception;

import org.springframework.http.HttpStatus;

/**
 * @author cjrequena
 *
 */
public class ConflictException extends ControllerRuntimeException {

  public ConflictException() {
    super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase());
  }

  public ConflictException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}
