package com.cjrequena.eventstore.sample.exception.service;

public class IllegalArgumentServiceException extends ServiceException {
  public IllegalArgumentServiceException(String message) {
    super(message);
  }
}