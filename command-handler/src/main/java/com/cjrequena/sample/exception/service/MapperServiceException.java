package com.cjrequena.sample.exception.service;


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
