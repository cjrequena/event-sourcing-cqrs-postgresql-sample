package com.cjrequena.sample.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
@JsonPropertyOrder(value = {
  "account_id",
  "amount"
})
@Schema
@Data
public class DepositVO {

  @JsonProperty(value = "account_id")
  private UUID accountId;

  @JsonProperty(value = "amount")
  private BigDecimal amount;

}
