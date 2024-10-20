package com.cjrequena.sample.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@ToString
@JsonPropertyOrder(value = {
  "account_id",
  "amount"
})
public class DebitVO implements Serializable {

  @JsonProperty(value = "account_id")
  private final UUID accountId;

  @JsonProperty(value = "amount")
  private final BigDecimal amount;

  // No setters to maintain immutability

}
