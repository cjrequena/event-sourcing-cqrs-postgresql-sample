package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.exception.service.AccountBalanceServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.vo.AccountVO;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Log4j2
@Component
@Transactional
public class CreateAccountCommandHandler extends CommandHandler<CreateAccountCommand> {

  @Autowired
  public CreateAccountCommandHandler(
    EventStoreService eventStoreService,
    AggregateFactory aggregateFactory,
    EventMapper eventMapper,
    EventStoreConfigurationProperties eventStoreConfigurationProperties) {
    super(eventStoreService, aggregateFactory, eventMapper, eventStoreConfigurationProperties);
  }

  @Override
  public void handle(@Nonnull Command command) {
    log.trace("Handling command of type {} for aggregate {}", command.getClass().getSimpleName(), command.getAggregateType());

    if (!(command instanceof CreateAccountCommand)) {
      throw new IllegalArgumentException("Expected command of type CreateAccountCommand but received " + command.getClass().getSimpleName());
    }

    // Safely cast the command and retrieve AccountVO
    AccountVO accountVO = ((CreateAccountCommand) command).getAccountVO();

    // Validate account balance
    if (accountVO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
      throw new AccountBalanceServiceException(
        String.format("Invalid account balance for account ID %s: Balance cannot be negative.", accountVO.getId())
      );
    }

    // Apply command and persist aggregate state
    try {
      Aggregate aggregate = retrieveOrInstantiateAggregate(command.getAggregateId());
      aggregate.applyCommand(command);
      eventStoreService.saveAggregate(aggregate);
      aggregate.markUnconfirmedEventsAsConfirmed();
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new OptimisticConcurrencyServiceException(ex.getMessage(), ex);
    }

    log.info("Successfully handled command {} and updated aggregate with ID {}", command.getClass().getSimpleName(), command.getAggregateId());
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
