package com.cjrequena.sample.shared.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

  @Override
  public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    try {
      String dateAsString = jsonParser.getText();
      if (dateAsString == null) {
        throw new IOException("OffsetDateTime argument is null.");
      }
      return OffsetDateTime.parse(dateAsString, FORMATTER);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage() + " - Invalid Format");
      throw ex;
    }
  }
}
