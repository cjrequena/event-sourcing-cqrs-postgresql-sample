package com.cjrequena.sample.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonPropertyOrder(value = {
  "account_id",
  "amount"
})
public class DebitVO implements Serializable {

  @JsonProperty(value = "account_id")
  private UUID accountId;

  @JsonProperty(value = "amount")
  private BigDecimal amount;

  // No setters to maintain immutability

}
