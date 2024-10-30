package com.cjrequena.sample;

import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
  "com.cjrequena.sample" // The main package
})
public class QueryHandlerMainApplication implements CommandLineRunner {

  public static void main(String... args) {
    SpringApplication.run(QueryHandlerMainApplication.class, args);
  }

  @SneakyThrows
  @Override
  public void run(String... args) throws Exception {

  }
}
