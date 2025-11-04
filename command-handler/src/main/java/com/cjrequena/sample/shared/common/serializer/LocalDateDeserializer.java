package com.cjrequena.sample.shared.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Log4j2
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  @Override
  public LocalDate deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
    try {
      String dateAsString = jsonParser.getText();
      if (dateAsString == null) {
        throw new IOException("LocalDate argument is null.");
      }
      return LocalDate.parse(dateAsString, FORMATTER);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage() + " - Invalid Format");
      throw ex;
    }
  }
}
