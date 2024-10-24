package com.cjrequena.sample;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.eventstore.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.repository.AggregateRepository;
import com.cjrequena.eventstore.sample.repository.AggregateSnapshotRepository;
import com.cjrequena.eventstore.sample.repository.EventRepository;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.component.CommandHandler;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.domain.command.CreditAccountCommand;
import com.cjrequena.sample.domain.command.DebitAccountCommand;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.CreditVO;
import com.cjrequena.sample.vo.DebitVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Log4j2
@SpringBootApplication
@RequiredArgsConstructor
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

  @Override
  public void run(String... args) throws JsonProcessingException {

    final Aggregate aggregateSS = this.eventStoreService.retrieveAggregateSnapshot(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), UUID.fromString("582d9889-d262-4a7e-ac3e-5d4c88c6e957"), null).get();
    final List<Event> eventList = this.eventMapper.toEventList(this.eventStoreService.retrieveEventsByAggregateId(aggregateSS.getAggregateId(), aggregateSS.getAggregateVersion(), null));
    aggregateSS.reconstituteFromConfirmedEvents(eventList);

    AccountVO accountVO = AccountVO.builder().owner("Carlos").balance(BigDecimal.valueOf(100)).build();
    Command createAccountCommand = CreateAccountCommand.builder()
      .accountVO(accountVO)
      .build();
    final UUID aggregateId = createAccountCommand.getAggregateId();


    Aggregate aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    aggregate.applyCommand(createAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }

    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    List<EventEntity> eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    List<Event> events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    Command creditAccountCommand = CreditAccountCommand.builder()
      .aggregateId(aggregateId)
      .creditVO(CreditVO.builder()
        .accountId(aggregateId)
        .amount(BigDecimal.valueOf(100))
        .build())
      .build();
    aggregate.applyCommand(creditAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(creditAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(creditAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(creditAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(creditAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(creditAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(creditAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    Command debittAccountCommand = DebitAccountCommand.builder()
      .aggregateId(aggregateId)
      .debitVO(DebitVO.builder()
        .accountId(aggregateId)
        .amount(BigDecimal.valueOf(100))
        .build())
      .build();
    aggregate.applyCommand(debittAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(debittAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(debittAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // --
    aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    eventEntities = this.eventStoreService.retrieveEventsByAggregateId(aggregateId, null, null);
    events = this.eventMapper.toEventList(eventEntities);
    aggregate.reconstituteFromConfirmedEvents(events);
    aggregate.applyCommand(debittAccountCommand);
    try {
      this.eventStoreService.saveAggregate(aggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();

    // Get the unconfirmed events pool
    List<Event> unconfirmedEventsPool = aggregate.getUnconfirmedEventsPool();

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
