package com.cjrequena.sample;

import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.repository.AggregateRepository;
import com.cjrequena.eventstore.sample.repository.AggregateSnapshotRepository;
import com.cjrequena.eventstore.sample.repository.EventRepository;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.mapper.EventMapper;
import com.cjrequena.sample.service.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@Log4j2
@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {
  "com.cjrequena.sample",  // The main package
  "com.cjrequena.eventstore.sample"  // Eventstore
})
public class CommandHandlerMainApplication implements CommandLineRunner {

  private final EventRepository eventRepository;
  private final AggregateRepository aggregateRepository;
  private final AggregateSnapshotRepository aggregateSnapshotRepository;
  private final List<CommandHandler<? extends Command>> commandHandlers;
  private final EventStoreService eventStoreService;
  private final EventMapper eventMapper;
  private final AggregateFactory aggregateFactory;

  public static void main(String... args) {
    SpringApplication.run(CommandHandlerMainApplication.class, args);
  }

  @SneakyThrows
  @Override
  public void run(String... args) {

    //    final Optional<Aggregate> optionalAggregateSS = this.eventStoreService.retrieveAggregateSnapshot(AggregateType.ACCOUNT_AGGREGATE.getClazz(),
    //      UUID.fromString("582d9889-d262-4a7e-ac3e-5d4c88c6e957"), null);
    //    optionalAggregateSS
    //      .map(aggregate -> {
    //        List<Event> eventList = eventMapper.mapToEventList(
    //          eventStoreService.retrieveEventsByAggregateId(aggregate.getAggregateId(), aggregate.getAggregateVersion(), null)
    //        );
    //        aggregate.reproduceFromEvents(eventList);
    //        return aggregate;
    //      });
    //
    //    AccountVO accountVO = AccountVO.builder().owner("Carlos").balance(BigDecimal.valueOf(100)).build();
    //    Command createAccountCommand = CreateAccountCommand.builder()
    //      .accountVO(accountVO)
    //      .build();
    //    final UUID aggregateId = createAccountCommand.getAggregateId();
    //
    //    Aggregate aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    aggregate.applyCommand(createAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    List<EventEntity> eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    List<Event> events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    Command creditAccountCommand = CreditAccountCommand.builder()
    //      .aggregateId(aggregateId)
    //      .creditVO(CreditVO.builder()
    //        .accountId(aggregateId)
    //        .amount(BigDecimal.valueOf(100))
    //        .build())
    //      .build();
    //    aggregate.applyCommand(creditAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(creditAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(creditAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(creditAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(creditAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(creditAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(creditAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    Command debittAccountCommand = DebitAccountCommand.builder()
    //      .aggregateId(aggregateId)
    //      .debitVO(DebitVO.builder()
    //        .accountId(aggregateId)
    //        .amount(BigDecimal.valueOf(100))
    //        .build())
    //      .build();
    //    aggregate.applyCommand(debittAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(debittAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(debittAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();
    //
    //    // --
    //    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getClazz(), aggregateId);
    //    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    //    events = this.eventMapper.mapToEventList(eventEntities);
    //    aggregate.reproduceFromEvents(events);
    //    aggregate.applyCommand(debittAccountCommand);
    //    try {
    //      this.eventStoreService.saveAggregate(aggregate);
    //    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
    //      log.error(ex);
    //    }
    //    aggregate.markUnconfirmedEventsAsConfirmed();

    // Get the unconfirmed events pool
    //    List<Event> unconfirmedEventsPool = aggregate.getUnconfirmedEventsPool();

    //    final List<EventEntity> eventEntities = this.eventRepository.retrieveEventsByAggregateId(UUID.fromString("9f53ce42-0381-4a15-bfc9-5ad939e8cf99"), null, null);
    //    List<Event> events = new ArrayList<>();
    //    for (EventEntity eventEntity : eventEntities) {
    //      if (eventEntity.getEventType().equals(EventType.ACCOUNT_CREATED_EVENT.toString())) {
    //        final AccountCreatedEvent accountCreatedEvent = this.eventMapper.toAccountCreatedEvent(eventEntity);
    //        events.add(accountCreatedEvent);
    //      } else if (eventEntity.getEventType().equals(EventType.ACCOUNT_CREDITED_EVENT.toString())) {
    //        final AccountCreditedEvent accountCreditedEvent = this.eventMapper.toAccountCreditedEvent(eventEntity);
    //        events.add(accountCreditedEvent);
    //      } else {
    //        final AccountDebitedEvent accountDebitedEvent = this.eventMapper.toAccountDebitedEvent(eventEntity);
    //        events.add(accountDebitedEvent);
    //      }
    //    }

    //    commandHandlers.stream()
    //      .filter(commandHandler -> commandHandler.getCommandType() == command.getClass())
    //      .findFirst()
    //      .ifPresentOrElse(commandHandler -> {
    //        log.info("Handling command {} with {}", command.getClass().getSimpleName(), commandHandler.getClass().getSimpleName());
    //        commandHandler.handle(command, accountAggregate);
    //      }, () -> {
    //        log.info("No specialized handler found with {}", command.getClass().getSimpleName());
    //      });

    log.debug("GOOD");
  }
}
