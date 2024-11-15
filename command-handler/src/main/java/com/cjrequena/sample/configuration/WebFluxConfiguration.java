package com.cjrequena.sample.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer {
  private final ObjectMapper mapper;

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer serverCodecConfigurer) {
    serverCodecConfigurer.defaultCodecs().jackson2JsonEncoder(
      new Jackson2JsonEncoder(mapper)
    );

    serverCodecConfigurer.defaultCodecs().jackson2JsonDecoder(
      new Jackson2JsonDecoder(mapper)
    );
  }

  @Override
  public void addCorsMappings(CorsRegistry cors) {
    cors.addMapping("/**")
      .allowedOrigins("*")
      .allowedMethods("*")
      .maxAge(3600)
      .exposedHeaders("total-count")
      .allowedHeaders("*");
  }
}
