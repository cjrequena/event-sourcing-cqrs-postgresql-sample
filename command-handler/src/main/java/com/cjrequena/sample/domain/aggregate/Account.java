package com.cjrequena.sample.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor // Lombok generates a no-args constructor
@AllArgsConstructor // Lombok generates an all-args constructor
@ToString
@JsonPropertyOrder(value = {
  "id",
  "owner",
  "balance",
  "version"
})
public class Account implements Serializable {

  @JsonProperty(value = "id")
  private UUID id;

  @JsonProperty(value = "owner")
  private String owner;

  @JsonProperty(value = "balance")
  private BigDecimal balance;

}
