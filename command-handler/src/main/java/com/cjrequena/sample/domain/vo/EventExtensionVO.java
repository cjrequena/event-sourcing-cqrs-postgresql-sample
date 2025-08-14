package com.cjrequena.sample.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
//@AllArgsConstructor
@ToString
@JsonPropertyOrder(value = {
  "trace_id"
})
public class EventExtensionVO implements Serializable {

  @JsonProperty(value = "trace_id")
  private final String traceId;

  // Constructor for deserialization
  @JsonCreator
  public EventExtensionVO(@JsonProperty("trace_id") String traceId) {
    this.traceId = traceId;
  }

  // You can add more custom fields if needed
  // private String customField;

  // No setters to maintain immutability

}
