package com.cjrequena.sample.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@ToString
@JsonPropertyOrder(value = {
  "account_id",
  "amount"
})
public class CreditVO implements Serializable {

  @JsonProperty(value = "account_id")
  private final UUID accountId;

  @JsonProperty(value = "amount")
  private final BigDecimal amount;

  // Constructor for deserialization
  @JsonCreator
  public CreditVO(@JsonProperty("account_id") UUID accountId, @JsonProperty("amount") BigDecimal amount) {
    this.accountId = accountId;
    this.amount = amount;
  }

  // No setters to maintain immutability

}
