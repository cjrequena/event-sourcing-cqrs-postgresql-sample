package com.cjrequena.sample.exception.api;

import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class NotImplementedApiException extends ApiException {
  public NotImplementedApiException() {
    super(HttpStatus.NOT_IMPLEMENTED);
  }

  public NotImplementedApiException(String message) {
    super(HttpStatus.NOT_IMPLEMENTED, message);
  }
}
