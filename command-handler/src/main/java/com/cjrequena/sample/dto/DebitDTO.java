package com.cjrequena.sample.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Represents a debit transaction")
public class DebitDTO  {

  @NotNull(message = "Amount is required")
  @JsonProperty("amount")
  @Schema(description = "The transaction amount", example = "100.00")
  private BigDecimal amount;
}
