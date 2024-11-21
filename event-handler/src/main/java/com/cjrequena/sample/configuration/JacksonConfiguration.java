package com.cjrequena.sample.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 */
@Configuration
public class JacksonConfiguration {

  //  spring.jackson.serialization.FAIL_ON_EMPTY_BEANS=false
  //  spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false
  //  spring.jackson.deserialization.FAIL_ON_UNKNOWN_PROPERTIES=false
  //  spring.jackson.deserialization.ACCEPT_SINGLE_VALUE_AS_ARRAY=true
  //  spring.jackson.mapper.ACCEPT_CASE_INSENSITIVE_PROPERTIES=true
  //  spring.jackson.defaultPropertyInclusion=NON_NULL

  public static Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.indentOutput(false);
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return builder;
  }

  public static ObjectMapper buildPrimaryObjectMapper() {
    ObjectMapper objectMapper = jackson2ObjectMapperBuilder()
      .serializationInclusion(NON_NULL)
      .serializationInclusion(NON_EMPTY)
      .failOnEmptyBeans(false)
      .failOnUnknownProperties(false)
      .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .featuresToDisable(MapperFeature.DEFAULT_VIEW_INCLUSION, DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
      .simpleDateFormat("yyyy-MM-dd")
      .modules(new JavaTimeModule())
      .build();

    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
    return objectMapper;
  }

  @Bean(name = {"primaryObjectMapper"})
  @Primary
  public ObjectMapper primaryObjectMapper() {
    return buildPrimaryObjectMapper();
  }

}
