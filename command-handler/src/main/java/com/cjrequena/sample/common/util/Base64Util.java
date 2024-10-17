package com.cjrequena.sample.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class Base64Util {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  // Method to convert object to Base64
  public static <T> String objectToJsonBase64(T obj) throws JsonProcessingException {
    // Convert the object to a JSON string
    String jsonString = objectMapper.writeValueAsString(obj);

    // Encode the JSON string to Base64
    return Base64.getEncoder().encodeToString(jsonString.getBytes());
  }

  // Method to decode Base64 string back to object
  public static <T> T jsonBase64ToObject(String base64String, Class<T> clazz) throws JsonProcessingException {
    // Decode the Base64 string to JSON string
    byte[] decodedBytes = Base64.getDecoder().decode(base64String);
    String jsonString = new String(decodedBytes);

    // Convert JSON string back to object
    return objectMapper.readValue(jsonString, clazz);
  }

  // Method to convert object to JSON string
  public static <T> String objectToJsonString(T obj) throws JsonProcessingException {
    // Convert the object to a JSON string
    return objectMapper.writeValueAsString(obj);
  }

  // Method to convert JSON string back to object
  public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) throws JsonProcessingException {
    // Convert JSON string back to object
    return objectMapper.readValue(jsonString, clazz);
  }
}
