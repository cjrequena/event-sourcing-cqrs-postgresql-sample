package com.cjrequena.sample.exception.service;


public class EventMapperServiceException extends RuntimeServiceException {

  public EventMapperServiceException(Throwable ex) {
    super(ex);
  }

  public EventMapperServiceException(String message) {
    super(message);
  }

  public EventMapperServiceException(String message, Throwable ex) {
    super(message, ex);
  }
}
