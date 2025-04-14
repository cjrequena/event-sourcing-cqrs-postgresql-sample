package com.cjrequena.sample.common.util;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider {

  @Getter
  private static ApplicationContext context;

  public ApplicationContextProvider(ApplicationContext applicationContext) {
    ApplicationContextProvider.context = applicationContext;
  }

}
