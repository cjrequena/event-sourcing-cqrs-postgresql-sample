package com.cjrequena.sample.service.projection;

import com.cjrequena.eventstore.sample.domain.model.aggregate.Aggregate;
import com.cjrequena.sample.domain.model.aggregate.Account;
import com.cjrequena.sample.domain.model.aggregate.AggregateType;
import com.cjrequena.sample.persistence.entity.postgresql.AccountEntity;
import com.cjrequena.sample.service.AccountService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class PostgresAccountProjectionHandler implements ProjectionHandler {

  private final AccountService accountService;

  @Override
  public void handle(Aggregate aggregate) {
    log.debug("Saving or Updating read model for aggregate {}", aggregate);
    final Account account = ((Account) aggregate);
    AccountEntity accountEntity = AccountEntity.builder()
      .id(account.getId())
      .owner(account.getOwner())
      .balance(account.getBalance())
      .version(aggregate.getAggregateVersion())
      .build();
    this.accountService.save(accountEntity);
  }

  @Nonnull
  @Override
  public AggregateType getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE;
  }
}
