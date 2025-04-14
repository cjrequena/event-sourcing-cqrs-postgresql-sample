package com.cjrequena.sample.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

public class GenericJsonConverter<T> implements AttributeConverter<T, String> {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private final Class<T> clazz;

  public GenericJsonConverter(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public String convertToDatabaseColumn(T attribute) {
    if (attribute == null) {
      return null;
    }
    try {
      // Convert the attribute (Object) to JSON String
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error serializing object to JSON", e);
    }
  }

  @Override
  public T convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return null;
    }
    try {
      // Convert the JSON String from the database back to the Java object
      return objectMapper.readValue(dbData, clazz);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error deserializing JSON to object", e);
    }
  }
}
