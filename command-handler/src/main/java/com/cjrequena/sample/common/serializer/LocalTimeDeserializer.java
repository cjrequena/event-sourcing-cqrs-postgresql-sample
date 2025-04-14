package com.cjrequena.sample.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

  @Override
  public LocalTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
    try {
      String dateAsString = jsonParser.getText();
      if (dateAsString == null) {
        throw new IOException("LocalTime argument is null.");
      }
      return LocalTime.parse(dateAsString, FORMATTER);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage() + " - Invalid Format");
      throw ex;
    }
  }
}
