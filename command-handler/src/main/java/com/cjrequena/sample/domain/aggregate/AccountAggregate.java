package com.cjrequena.sample.domain.aggregate;

import com.cjrequena.sample.domain.event.AccountCreatedEvent;
import com.cjrequena.sample.vo.AccountVO;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class AccountAggregate extends Aggregate {

  private AccountVO account;

  public AccountAggregate(@NonNull UUID aggregateId, long version) {
    super(aggregateId, version);
  }

  public void apply(AccountCreatedEvent event) {
    this.account  = event.getAccount();
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.toString();
  }
}
