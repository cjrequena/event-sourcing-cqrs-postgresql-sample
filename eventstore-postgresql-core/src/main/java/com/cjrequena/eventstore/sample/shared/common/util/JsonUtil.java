package com.cjrequena.eventstore.sample.shared.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Utility class for working with JSON and Jackson ObjectMapper operations.
 * Centralizes JSON serialization, deserialization, and patching logic.
 */
public final class JsonUtil {

  // region === ObjectMapper Initialization ===
  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new Jackson2ObjectMapperBuilder()
      .indentOutput(false)
      .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
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
  }

  private JsonUtil() {
    // Prevent instantiation
  }
  // endregion

  // region === Basic JSON Conversion ===

  /**
   * Converts an object to a JSON string.
   */
  public static <T> String objectToJsonString(T object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  /**
   * Converts a JSON string back to an object.
   */
  public static <T> T jsonStringToObject(String jsonString, Class<T> clazz)
    throws JsonProcessingException {
    return objectMapper.readValue(jsonString, clazz);
  }

  /**
   * Converts an object to a Base64-encoded JSON string.
   */
  public static <T> String objectToJsonBase64(T object) throws JsonProcessingException {
    String jsonString = objectMapper.writeValueAsString(object);
    return Base64.getEncoder().encodeToString(jsonString.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Decodes a Base64 JSON string back to an object.
   */
  public static <T> T jsonBase64ToObject(String base64, Class<T> clazz)
    throws JsonProcessingException {
    byte[] decodedBytes = Base64.getDecoder().decode(base64);
    String jsonString = new String(decodedBytes, StandardCharsets.UTF_8);
    return objectMapper.readValue(jsonString, clazz);
  }

  /**
   * Reads a JSON file from resources and returns its string representation.
   */
  public static String jsonFileToJsonString(String resourcePath) throws IOException {
    try (InputStream inputStream = JsonUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
      if (inputStream == null) {
        throw new IllegalArgumentException("File not found in resources: " + resourcePath);
      }
      ObjectNode node = (ObjectNode) objectMapper.readTree(inputStream);
      return objectMapper.writeValueAsString(node);
    }
  }
  // endregion

  // region === JsonNode Conversion ===

  /**
   * Converts an object into a JsonNode.
   */
  public static <T> JsonNode objectToJsonNode(T object) {
    return objectMapper.valueToTree(object);
  }

  /**
   * Converts a JsonNode back into an object of the specified type.
   */
  public static <T> T jsonNodeToObject(JsonNode node, Class<T> clazz) throws JsonProcessingException {
    return objectMapper.treeToValue(node, clazz);
  }

  // endregion

}
