package com.cjrequena.sample.domain.exception;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class AccountNotFoundException extends DomainRuntimeException {
  public AccountNotFoundException(String message) {
    super(message);
  }
}
