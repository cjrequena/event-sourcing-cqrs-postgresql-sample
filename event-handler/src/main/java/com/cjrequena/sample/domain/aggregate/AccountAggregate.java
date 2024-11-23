package com.cjrequena.sample.domain.aggregate;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.domain.event.AccountCreatedEvent;
import com.cjrequena.sample.domain.event.AccountCreditedEvent;
import com.cjrequena.sample.domain.event.AccountDebitedEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class AccountAggregate extends Aggregate {

  @JsonProperty("account")
  private Account account;

  @Builder
  @JsonCreator
  public AccountAggregate(@NonNull @JsonProperty("aggregate_id") UUID aggregateId, @JsonProperty("aggregate_version") long aggregateVersion) {
    super(aggregateId, aggregateVersion);
  }

  public void applyEvent(AccountCreatedEvent event) {
    this.account = Account.builder()
      .id(event.getData().getId())
      .owner(event.getData().getOwner())
      .balance(event.getData().getBalance())
      .build();
  }

  public void applyEvent(AccountCreditedEvent event) {
    this.account.setBalance(this.account.getBalance().add(event.getData().getAmount()));
  }

  public void applyEvent(AccountDebitedEvent event) {
    this.account.setBalance(this.account.getBalance().subtract(event.getData().getAmount()));
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.getType();
  }
}
