package com.cjrequena.sample.exception.api;

import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class NotAcceptableApiException extends ApiException {
  public NotAcceptableApiException() {
    super(HttpStatus.NOT_ACCEPTABLE);
  }
}
