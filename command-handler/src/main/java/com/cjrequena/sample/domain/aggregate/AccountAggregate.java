package com.cjrequena.sample.domain.aggregate;

import com.cjrequena.sample.common.util.Base64Util;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.domain.command.CreditAccountCommand;
import com.cjrequena.sample.domain.command.DebitAccountCommand;
import com.cjrequena.sample.domain.event.AccountCreatedEvent;
import com.cjrequena.sample.domain.event.AccountCreditedEvent;
import com.cjrequena.sample.domain.event.AccountDebitedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class AccountAggregate extends Aggregate {

  private Account account;

  @Builder
  public AccountAggregate(@NonNull UUID aggregateId, long version) {
    super(aggregateId, version);
  }

  public void applyCommand(CreateAccountCommand command) throws JsonProcessingException {
    applyUnconfirmedEvent(AccountCreatedEvent.builder()
      .aggregateId(command.getAggregateId())
      .aggregateVersion(getNextAggregateVersion())
      .dataContentType("application/json")
      .data(command.getAccountVO())
      .dataBase64(Base64Util.objectToJsonBase64(command.getAccountVO()))
      .build());
  }

  public void applyCommand(CreditAccountCommand command) throws JsonProcessingException {
    applyUnconfirmedEvent(AccountCreditedEvent.builder()
      .aggregateId(command.getAggregateId())
      .aggregateVersion(getNextAggregateVersion())
      .dataContentType("application/json")
      .credit(command.getCreditVO())
      .dataBase64(Base64Util.objectToJsonBase64(command.getCreditVO()))
      .build());
  }

  public void applyCommand(DebitAccountCommand command) throws JsonProcessingException {
    applyUnconfirmedEvent(AccountDebitedEvent.builder()
      .aggregateId(command.getAggregateId())
      .aggregateVersion(getNextAggregateVersion())
      .dataContentType("application/json")
      .credit(command.getDebitVO())
      .dataBase64(Base64Util.objectToJsonBase64(command.getDebitVO()))
      .build());
  }

  public void apply(AccountCreatedEvent event) {
    this.account = Account.builder()
      .id(event.getAccountVO().getId())
      .owner(event.getAccountVO().getOwner())
      .balance(event.getAccountVO().getBalance())
      .build();
  }

  public void apply(AccountCreditedEvent event) {
    this.account.setBalance(this.account.getBalance().add(event.getCredit().getAmount()));
  }

  public void apply(AccountDebitedEvent event) {
    this.account.setBalance(this.account.getBalance().subtract(event.getCredit().getAmount()));
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.toString();
  }
}
