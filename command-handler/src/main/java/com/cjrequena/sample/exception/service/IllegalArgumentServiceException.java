package com.cjrequena.sample.exception.service;


public class IllegalArgumentServiceException extends RuntimeServiceException {
  public IllegalArgumentServiceException(String message) {
    super(message);
  }
}
