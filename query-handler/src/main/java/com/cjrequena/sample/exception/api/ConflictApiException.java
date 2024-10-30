package com.cjrequena.sample.exception.api;

import org.springframework.http.HttpStatus;

/**
 * @author cjrequena
 *
 */
public class ConflictApiException extends ApiException {

  public ConflictApiException() {
    super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase());
  }

  public ConflictApiException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}
