package com.cjrequena.sample.exception.service;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class AccountNotFoundServiceException extends RuntimeException {
  public AccountNotFoundServiceException(String message) {
    super(message);
  }
}
