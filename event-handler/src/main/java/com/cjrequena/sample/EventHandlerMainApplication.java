package com.cjrequena.sample;

import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {
  "com.cjrequena.sample",  // The main package
  "com.cjrequena.eventstore.sample"  // Eventstore
})
public class EventHandlerMainApplication implements CommandLineRunner {

  public static void main(String... args) {
    SpringApplication.run(EventHandlerMainApplication.class, args);
  }

  @SneakyThrows
  @Override
  public void run(String... args) throws Exception {

  }
}
