package com.cjrequena.sample.exception.service;

import com.cjrequena.eventstore.sample.exception.service.RuntimeServiceException;

public class MapperServiceException extends RuntimeServiceException {

  public MapperServiceException(Throwable ex) {
    super(ex);
  }

  public MapperServiceException(String message) {
    super(message);
  }

  public MapperServiceException(String message, Throwable ex) {
    super(message, ex);
  }
}
