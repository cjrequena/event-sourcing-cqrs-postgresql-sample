package com.cjrequena.sample.exception;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class OptimisticConcurrencyServiceException extends ServiceException {
  public OptimisticConcurrencyServiceException(String message) {
    super(message);
  }
}
