package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.aggregate.AccountAggregate;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.entity.mongo.MongoAccountEntity;
import com.cjrequena.sample.entity.postgresql.AccountEntity;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.service.AccountService;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional
@Log4j2
public class AccountEventHandler extends AsyncEventHandler {

  private final AccountService accountService;

  @Autowired
  public AccountEventHandler(
    EventStoreService eventStoreService,
    AggregateFactory aggregateFactory,
    EventMapper eventMapper,
    EventStoreConfigurationProperties eventStoreConfigurationProperties,
    AccountService accountService) {
    super(eventStoreService, aggregateFactory, eventMapper, eventStoreConfigurationProperties);
    this.accountService = accountService;
  }

  @Override
  public void handle(List<EventEntity> eventEntityList) {
    final List<Event> events = this.eventMapper.mapToEventList(eventEntityList);

    // Map to store only the latest event by aggregate ID
    Map<UUID, Event> latestEventBysAggregateIdMap = new HashMap<>();

    for (Event event : events) {
      log.info("A new event {} is being handle for aggregate  with ID '{}' and aggregate version {}", event.getEventType(), event.getAggregateId(), event.getAggregateVersion());
      // Here is to set the business logic to send the incoming event through an integration channel, e.g. Kafka, SNS, SQS, AWS Lambda, Webhook, etc.

      // Here we store in a map just the last event receive by a specific aggregateId
      latestEventBysAggregateIdMap.put(event.getAggregateId(), event); // Overwrite if aggregate ID already exists
    }

    // Collect only the last event of each aggregate ID
    List<Event> latestEventBysAggregateIdList = new ArrayList<>(latestEventBysAggregateIdMap.values());

    //
    for (Event event : latestEventBysAggregateIdList) {
      final AccountAggregate aggregate = (AccountAggregate) this.retrieveOrInstantiateAggregate(event.getAggregateId());
      log.info("Preparing to save or update in the projection database the aggregate {}", aggregate);

      // Store the aggregate into the projection database.
      AccountEntity accountEntity = AccountEntity.builder()
        .id(aggregate.getAccount().getId())
        .owner(aggregate.getAccount().getOwner())
        .balance(aggregate.getAccount().getBalance())
        .version(aggregate.getAggregateVersion())
        .build();
      this.accountService.save(accountEntity);

      // Mongo
      MongoAccountEntity mongoAccountEntity = MongoAccountEntity.builder()
        .id(aggregate.getAccount().getId())
        .owner(aggregate.getAccount().getOwner())
        .balance(aggregate.getAccount().getBalance())
        .version(aggregate.getAggregateVersion())
        .build();
      this.accountService.save(mongoAccountEntity).subscribe();

    }
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.getType();
  }

}
