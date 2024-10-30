package com.cjrequena.sample.exception.api;

import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class NotFoundApiException extends ApiException {
  public NotFoundApiException() {
    super(HttpStatus.NOT_FOUND);
  }

  public NotFoundApiException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
