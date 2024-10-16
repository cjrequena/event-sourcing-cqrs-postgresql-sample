package com.cjrequena.sample.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(value = {
  "trace_id"
})
@Schema
public class EventExtensionVO {

  @JsonProperty(value = "trace_id")
  private String traceId;

  // You can add more custom fields if needed
  // private String customField;

}
