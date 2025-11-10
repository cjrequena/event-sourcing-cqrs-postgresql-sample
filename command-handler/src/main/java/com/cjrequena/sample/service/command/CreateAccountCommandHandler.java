package com.cjrequena.sample.service.command;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.exception.OptimisticConcurrencyException;
import com.cjrequena.eventstore.sample.domain.model.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.model.command.Command;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.exception.AccountBalanceException;
import com.cjrequena.sample.domain.mapper.EventMapper;
import com.cjrequena.sample.domain.model.aggregate.AggregateType;
import com.cjrequena.sample.domain.model.command.CreateAccountCommand;
import com.cjrequena.sample.domain.model.vo.AccountVO;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@Transactional
public class CreateAccountCommandHandler extends CommandHandler<CreateAccountCommand> {

  @Autowired
  public CreateAccountCommandHandler(
    EventStoreService eventStoreService,
    EventMapper eventMapper,
    EventStoreConfigurationProperties eventStoreConfigurationProperties) {
    super(eventStoreService, eventMapper, eventStoreConfigurationProperties);
  }

  @Override
  public Aggregate handle(@Nonnull Command command) {
    log.trace("Handling command of type {} for aggregate {}", command.getClass().getSimpleName(), command.getAggregateType());

    if (!(command instanceof CreateAccountCommand)) {
      throw new IllegalArgumentException("Expected command of type CreateAccountCommand but received " + command.getClass().getSimpleName());
    }

    // Get the current aggregate.
    Aggregate aggregate = retrieveOrInstantiateAggregate(command.getAggregateId());
    AccountVO accountVO = ((CreateAccountCommand) command).getAccountVO();

    // Validate account balance
    if (accountVO.isBalanceLessThanZero()) {
      throw new AccountBalanceException(
        String.format("Invalid account balance for account ID %s: Balance cannot be negative.", accountVO.id())
      );
    }

    // Apply command and persist aggregate state
    try {
      aggregate.applyCommand(command);
      eventStoreService.saveAggregate(aggregate);
      aggregate.markUnconfirmedEventsAsConfirmed();
    } catch (OptimisticConcurrencyException ex) {
      throw new com.cjrequena.sample.domain.exception.OptimisticConcurrencyException(ex.getMessage(), ex);
    }
    log.info("Successfully handled command {} for aggregate with ID {}", command.getClass().getSimpleName(), command.getAggregateId());
    return aggregate;
  }

  @Nonnull
  @Override
  public Class<CreateAccountCommand> getCommandType() {
    return CreateAccountCommand.class;
  }

  @Nonnull
  @Override
  public AggregateType getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE;
  }

}
