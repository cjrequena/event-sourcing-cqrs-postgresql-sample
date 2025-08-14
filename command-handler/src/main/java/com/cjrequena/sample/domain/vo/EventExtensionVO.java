package com.cjrequena.sample.domain.vo;

import com.cjrequena.sample.exception.service.IllegalArgumentServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EventExtensionVO(
  @NotNull
  String traceId
) implements Serializable {
  /**
   * Canonical constructor with validation and data normalization.
   */
  public EventExtensionVO {
    if (traceId == null) {
      throw new IllegalArgumentServiceException("TraceId is required");
    }
  }
}
