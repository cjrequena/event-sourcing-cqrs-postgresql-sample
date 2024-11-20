package com.cjrequena.sample.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Apply to the entire class
@JsonPropertyOrder({
  "id",
  "owner",
  "balance",
  "version"
})
@Schema(description = "Represents an account in the system")
public class AccountDTO implements Serializable {

  @JsonProperty("id")
  @Schema(accessMode = READ_ONLY, description = "The unique identifier for the account")
  private UUID id;

  @NotNull(message = "Owner is a required field")
  @JsonProperty("owner")
  @Schema(description = "The owner of the account")
  private String owner;

  @NotNull(message = "Balance is a required field")
  @JsonProperty("balance")
  @Schema(description = "The balance of the account", example = "1000.00")
  private BigDecimal balance;

}
