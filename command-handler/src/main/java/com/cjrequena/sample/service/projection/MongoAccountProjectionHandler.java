package com.cjrequena.sample.service.projection;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.domain.aggregate.Account;
import com.cjrequena.sample.domain.aggregate.AccountAggregate;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.entity.mongo.MongoAccountEntity;
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
public class MongoAccountProjectionHandler implements ProjectionHandler {

  private final AccountService accountService;

  @Override
  public void handle(Aggregate aggregate) {
    log.debug("Saving or Updating read model for aggregate {}", aggregate);
    final Account account = ((AccountAggregate) aggregate).getAccount();
    MongoAccountEntity accountEntity = MongoAccountEntity.builder()
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
