package com.cjrequena.sample.exception.api;

import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class BadFiltersApiException extends ApiException {
  public BadFiltersApiException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
