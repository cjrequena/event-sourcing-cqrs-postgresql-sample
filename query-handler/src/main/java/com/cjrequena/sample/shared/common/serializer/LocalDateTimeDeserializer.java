package com.cjrequena.sample.shared.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
@Log4j2
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  /**
   *
   * @param parser
   * @param context
   * @return
   * @throws IOException
   */
  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    try {
      return LocalDateTime.parse(parser.readValueAs(String.class));
    } catch (Exception ex) {
      log.error("{}", ex.getMessage() + " - Invalid Date Format");
      throw ex;
    }
  }
}
