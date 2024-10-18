package com.cjrequena.sample.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data // Lombok generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok generates a no-args constructor
@AllArgsConstructor // Lombok generates an all-args constructor
@Builder
@JsonPropertyOrder(value = {
  "id",
  "owner",
  "balance",
  "version"
})
public class AccountVO implements Serializable {

  @JsonProperty(value = "id")
  private UUID id;

  @JsonProperty(value = "owner")
  private String owner;

  @JsonProperty(value = "balance")
  private BigDecimal balance;

  @JsonProperty(value = "version")
  private long version;

}
