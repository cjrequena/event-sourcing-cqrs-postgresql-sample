package com.cjrequena.sample.domain.aggregate;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.common.util.JsonUtil;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.domain.command.CreditAccountCommand;
import com.cjrequena.sample.domain.command.DebitAccountCommand;
import com.cjrequena.sample.domain.event.AccountCreatedEvent;
import com.cjrequena.sample.domain.event.AccountCreditedEvent;
import com.cjrequena.sample.domain.event.AccountDebitedEvent;
import com.cjrequena.sample.domain.vo.EventExtensionVO;
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

import java.math.BigDecimal;
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
      .dataContentType("application/json")
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
      .dataContentType("application/json")
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
      .dataContentType("application/json")
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

    BigDecimal amount = event.getData().getAmount();
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Credit amount must be positive");
    }

    this.balance = this.balance.add(amount);
  }

  public void applyEvent(AccountDebitedEvent event) {
    if (this.balance == null) {
      this.balance = BigDecimal.ZERO;
    }

    BigDecimal amount = event.getData().getAmount();
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Debit amount must be positive");
    }

    this.balance = this.balance.subtract(amount);
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.getType();
  }
}
