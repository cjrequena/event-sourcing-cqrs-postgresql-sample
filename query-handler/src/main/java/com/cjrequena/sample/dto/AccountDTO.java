package com.cjrequena.sample.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
@Getter
@Builder
@AllArgsConstructor
@ToString
@JsonPropertyOrder(value = {
  "id",
  "owner",
  "balance",
  "version"
})
@Schema
public class AccountDTO implements Serializable {

  @JsonProperty(value = "id")
  @Schema(accessMode = READ_ONLY)
  private UUID id;

  @NotNull(message = "owner is a required field")
  @JsonProperty(value = "owner", required = true)
  private String owner;

  @NotNull(message = "balance is a required field")
  @JsonProperty(value = "balance", required = true)
  private BigDecimal balance;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty(value = "version")
  @Schema(accessMode = READ_ONLY)
  private Long version;

}
