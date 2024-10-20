package com.cjrequena.sample.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@ToString
@JsonPropertyOrder(value = {
  "trace_id"
})
@Schema
public class EventExtensionVO implements Serializable {

  @JsonProperty(value = "trace_id")
  private final String traceId;

  // You can add more custom fields if needed
  // private String customField;

  // No setters to maintain immutability

}
