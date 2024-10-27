package com.cjrequena.sample.service;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.component.CommandHandler;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.exception.service.CommandHandlerNotFoundServiceException;
import com.cjrequena.sample.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandHandlerService {

  private final EventStoreService eventStoreService;
  private final List<CommandHandler<? extends Command>> commandHandlers;
  private final EventMapper eventMapper;
  private final EventStoreConfigurationProperties eventStoreConfigurationProperties;
  private final AggregateFactory aggregateFactory;

  public void handler(Command command) {
    log.info("Processing command {}", command);

    final UUID aggregateId = command.getAggregateId();
    Aggregate aggregate = retrieveOrCreateAggregate(aggregateId, command);

    commandHandlers.stream()
      .filter(commandHandler -> commandHandler.getCommandType() == command.getClass())
      .findFirst()
      .ifPresentOrElse(commandHandler -> {
        log.info("Handling command {} with {}", command.getClass().getSimpleName(), commandHandler.getClass().getSimpleName());
        commandHandler.handle(command, aggregate);
      }, () -> {
        log.info("No specialized handler found with {}", command.getClass().getSimpleName());
        throw new CommandHandlerNotFoundServiceException("No specialized handler found for command: " + command.getClass().getSimpleName());
      });
  }

  private Aggregate retrieveOrCreateAggregate(UUID aggregateId, Command command) {
    final EventStoreConfigurationProperties.SnapshotProperties snapshotConfiguration = eventStoreConfigurationProperties.getSnapshot(AggregateType.ACCOUNT_AGGREGATE.toString());
    if (snapshotConfiguration.enabled()) {
      return retrieveAggregateFromSnapshot(aggregateId, command)
        .orElseGet(() -> createAndReproduceAggregate(aggregateId, command));
    } else {
      return createAndReproduceAggregate(aggregateId, command);
    }
  }

  private Optional<Aggregate> retrieveAggregateFromSnapshot(UUID aggregateId, Command command) {
    Optional<Aggregate> optionalAggregate = eventStoreService.retrieveAggregateSnapshot(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId, null);
    return optionalAggregate.map(aggregate -> {
      List<Event> events = retrieveEvents(aggregateId, aggregate.getAggregateVersion());
      aggregate.reproduceFromEvents(events);
      return aggregate;
    });
  }

  private Aggregate createAndReproduceAggregate(UUID aggregateId, Command command) {
    log.info("Snapshot not found for Aggregate ID: {}. Reconstituting from events.", aggregateId);
    Aggregate aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    List<Event> events = retrieveEvents(aggregateId, null);
    aggregate.reproduceFromEvents(events);
    return aggregate;
  }

  private List<Event> retrieveEvents(UUID aggregateId, Long fromVersion) {
    return eventMapper.mapToEventList(eventStoreService.retrieveEventsByAggregateId(aggregateId, fromVersion, null));
  }
}
