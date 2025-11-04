package com.cjrequena.sample.controller.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public abstract class ControllerRuntimeException extends RuntimeException {
  private final HttpStatus httpStatus;

  /**
   * Constructor
   * @param httpStatus status code
   */
  public ControllerRuntimeException(HttpStatus httpStatus) {
    super(httpStatus.getReasonPhrase());
    this.httpStatus = httpStatus;
  }

  /**
   * Constructor
   * @param httpStatus status code
   * @param message Custom message
   */
  public ControllerRuntimeException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  /**
   * Constructor
   * @param httpStatus status code
   * @param message custom message
   * @param throwable Exception
   */
  public ControllerRuntimeException(HttpStatus httpStatus, String message, Throwable throwable) {
    super(message, throwable);
    this.httpStatus = httpStatus;
  }

}
