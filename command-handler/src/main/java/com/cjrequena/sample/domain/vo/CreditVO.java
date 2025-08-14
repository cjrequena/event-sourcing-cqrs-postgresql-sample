package com.cjrequena.sample.domain.vo;

import com.cjrequena.sample.exception.service.AmountServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreditVO(

  @NotNull
  UUID accountId,

  @NotNull
  @DecimalMin(value = "0.00", message = "Amount cannot be negative")
  BigDecimal amount

) implements Serializable {

  public CreditVO {
    // Auto-generate ID if null
    if (accountId == null) {
      throw new IllegalArgumentException("AccountId is required");
    }

    // Set default amount if null and normalize to 2 decimal places
    if (amount == null) {
      amount = BigDecimal.ZERO;
    } else {
      amount = amount.setScale(2, RoundingMode.HALF_UP);
      if (amount.compareTo(BigDecimal.ZERO) <=0) {
        throw new AmountServiceException("Credit amount must be positive");
      }
    }
  }

  /**
   * Checks if the amount is zero.
   *
   * @return true if amount is zero, false otherwise
   */
  public boolean isAmountEqualOrLessThanZero() {
    return amount != null && amount.compareTo(BigDecimal.ZERO) <= 0;
  }

}
