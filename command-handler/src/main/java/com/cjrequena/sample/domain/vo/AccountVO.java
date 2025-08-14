package com.cjrequena.sample.domain.vo;

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
  "id",
  "owner",
  "balance",
  "version"
})
public class AccountVO implements Serializable {

  @JsonProperty(value = "id")
  private final UUID id;

  @JsonProperty(value = "owner")
  private final String owner;

  @JsonProperty(value = "balance")
  private final BigDecimal balance;

  // Constructor for deserialization
  @JsonCreator
  public AccountVO(
    @JsonProperty("id") UUID accountId,
    @JsonProperty(value = "owner") String owner,
    @JsonProperty("balance") BigDecimal balance
  ) {
    this.id = accountId;
    this.owner = owner;
    this.balance = balance;
  }

  // No setters to maintain immutability

}
