package com.cjrequena.sample.domain.aggregate;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.common.util.JsonUtil;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.domain.command.CreditAccountCommand;
import com.cjrequena.sample.domain.command.DebitAccountCommand;
import com.cjrequena.sample.domain.event.AccountCreatedEvent;
import com.cjrequena.sample.domain.event.AccountCreditedEvent;
import com.cjrequena.sample.domain.event.AccountDebitedEvent;
import com.cjrequena.sample.vo.EventExtensionVO;
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

  public void apply(AccountCreatedEvent event) {
    this.account = Account.builder()
      .id(event.getData().getId())
      .owner(event.getData().getOwner())
      .balance(event.getData().getBalance())
      .build();
  }

  public void apply(AccountCreditedEvent event) {
    this.account.setBalance(this.account.getBalance().add(event.getData().getAmount()));
  }

  public void apply(AccountDebitedEvent event) {
    this.account.setBalance(this.account.getBalance().subtract(event.getData().getAmount()));
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.toString();
  }
}
