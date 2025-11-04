package com.cjrequena.sample.domain.model.aggregate;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.domain.exception.AmountException;
import com.cjrequena.sample.domain.model.command.CreateAccountCommand;
import com.cjrequena.sample.domain.model.command.CreditAccountCommand;
import com.cjrequena.sample.domain.model.command.DebitAccountCommand;
import com.cjrequena.sample.domain.model.event.AccountCreatedEvent;
import com.cjrequena.sample.domain.model.event.AccountCreditedEvent;
import com.cjrequena.sample.domain.model.event.AccountDebitedEvent;
import com.cjrequena.sample.domain.model.vo.CreditVO;
import com.cjrequena.sample.domain.model.vo.DebitVO;
import com.cjrequena.sample.domain.model.vo.EventExtensionVO;
import com.cjrequena.sample.shared.common.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Account extends Aggregate {

  private UUID id;
  private String owner;
  private BigDecimal balance;

  @Builder
  @JsonCreator
  public Account(@NonNull @JsonProperty("aggregate_id") UUID aggregateId, @JsonProperty("aggregate_version") long aggregateVersion) {
    super(aggregateId, aggregateVersion);
  }

  public void applyCommand(CreateAccountCommand command) throws JsonProcessingException {

    EventExtensionVO eventExtension = EventExtensionVO.builder().traceId(UUID.randomUUID().toString()).build(); // TODO implement EventExtension

    applyUnconfirmedEvent(AccountCreatedEvent.builder()
      .aggregateId(command.getAggregateId())
      .aggregateVersion(getNextAggregateVersion())
      .dataContentType(MediaType.APPLICATION_JSON_VALUE)
      .data(command.getAccountVO())
      .dataBase64(JsonUtil.objectToJsonBase64(command.getAccountVO()))
      .extension(eventExtension)
      .build());
  }

  public void applyCommand(CreditAccountCommand command) throws JsonProcessingException {

    EventExtensionVO eventExtension = EventExtensionVO.builder().traceId(UUID.randomUUID().toString()).build(); // TODO implement EventExtension

    applyUnconfirmedEvent(AccountCreditedEvent.builder()
      .aggregateId(command.getAggregateId())
      .aggregateVersion(getNextAggregateVersion())
      .dataContentType(MediaType.APPLICATION_JSON_VALUE)
      .data(command.getCreditVO())
      .dataBase64(JsonUtil.objectToJsonBase64(command.getCreditVO()))
      .extension(eventExtension)
      .build());
  }

  public void applyCommand(DebitAccountCommand command) throws JsonProcessingException {
    EventExtensionVO eventExtension = EventExtensionVO.builder().traceId(UUID.randomUUID().toString()).build(); // TODO implement EventExtension

    applyUnconfirmedEvent(AccountDebitedEvent.builder()
      .aggregateId(command.getAggregateId())
      .aggregateVersion(getNextAggregateVersion())
      .dataContentType(MediaType.APPLICATION_JSON_VALUE)
      .data(command.getDebitVO())
      .dataBase64(JsonUtil.objectToJsonBase64(command.getDebitVO()))
      .extension(eventExtension)
      .build());
  }

  public void applyEvent(AccountCreatedEvent event) {
    this.id = event.getData().id();
    this.owner = event.getData().owner();
    this.balance = event.getData().balance();
  }

  public void applyEvent(AccountCreditedEvent event) {
    if (this.balance == null) {
      this.balance = BigDecimal.ZERO;
    }

    final CreditVO creditVO = event.getData();

    if (creditVO.isAmountEqualOrLessThanZero()) {
      throw new AmountException("Invalid credit amount: The amount must be greater than zero.");
    }

    this.balance = this.balance.add(creditVO.amount());
  }

  public void applyEvent(AccountDebitedEvent event) {
    if (this.balance == null) {
      this.balance = BigDecimal.ZERO;
    }

    final DebitVO debitVO = event.getData();

    if (debitVO.isAmountEqualOrLessThanZero()) {
      throw new AmountException("Invalid debit amount: The amount must be greater than zero.");
    }

    this.balance = this.balance.subtract(debitVO.amount());
  }

  /**
   * Checks if the account balance is zero.
   *
   * @return true if balance is zero, false otherwise
   */
  public boolean isBalanceEqualToZero() {
    return balance != null && balance.compareTo(BigDecimal.ZERO) == 0;
  }

  /**
   * Checks if the account balance is less than zero.
   *
   * @return true if balance is zero, false otherwise
   */
  public boolean isBalanceLessThanZero() {
    return balance != null && balance.compareTo(BigDecimal.ZERO) < 0;
  }

  /**
   * Checks if the account has sufficient balance for a given amount.
   *
   * @param amount the amount to check
   * @return true if balance is sufficient, false otherwise
   */
  public boolean hasSufficientBalance(BigDecimal amount) {
    return balance != null && amount != null && balance.compareTo(amount) >= 0;
  }

  /**
   * Gets the balance as a formatted string for display purposes.
   *
   * @return formatted balance string
   */
  public String getFormattedBalance() {
    return balance != null ? balance.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.getType();
  }
}
