package com.cjrequena.sample.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonPropertyOrder({
  "id",
  "owner",
  "balance",
  "version"
})
@Schema(description = "Represents an account in the system")
public class AccountDTO implements Serializable {

  @Schema(accessMode = READ_ONLY, description = "The unique identifier for the account")
  private UUID id;

  @NotNull(message = "Owner is a required field")
  @Schema(description = "The owner of the account")
  private String owner;

  @NotNull(message = "Balance is a required field")
  @Schema(description = "The balance of the account", example = "1000.00")
  private BigDecimal balance;

}
