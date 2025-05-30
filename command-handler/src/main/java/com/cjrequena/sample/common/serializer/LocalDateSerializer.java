package com.cjrequena.sample.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  /**
   *
   * @param value
   * @param generator
   * @param provider
   * @throws IOException
   */
  @Override
  public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider) throws IOException {
    generator.writeString(value.format(FORMATTER));
  }
}
