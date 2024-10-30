package com.cjrequena.sample.exception.api;

import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class BadRequestApiException extends ApiException {
  public BadRequestApiException() {
    super(HttpStatus.BAD_REQUEST);
  }

  public BadRequestApiException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
