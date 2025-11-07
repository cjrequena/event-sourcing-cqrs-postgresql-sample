package com.cjrequena.sample.service.command;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.exception.AggregateNotFoundException;
import com.cjrequena.sample.domain.exception.AmountException;
import com.cjrequena.sample.domain.exception.OptimisticConcurrencyException;
import com.cjrequena.sample.domain.mapper.EventMapper;
import com.cjrequena.sample.domain.model.aggregate.AggregateType;
import com.cjrequena.sample.domain.model.command.CreditAccountCommand;
import com.cjrequena.sample.domain.model.vo.CreditVO;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@Transactional
public class CreditAccountCommandHandler extends CommandHandler<CreditAccountCommand> {

  @Autowired
  public CreditAccountCommandHandler(
    EventStoreService eventStoreService,
    EventMapper eventMapper,
    EventStoreConfigurationProperties eventStoreConfigurationProperties) {
    super(eventStoreService, eventMapper, eventStoreConfigurationProperties);
  }

  @Override
  public Aggregate handle(@Nonnull Command command) {
    log.trace("Handling command of type {} for aggregate {}", command.getClass().getSimpleName(), command.getAggregateType());

    if (!(command instanceof CreditAccountCommand)) {
      throw new IllegalArgumentException("Expected command of type CreditAccountCommand but received " + command.getClass().getSimpleName());
    }

    if (!this.eventStoreService.verifyIfAggregateExist(command.getAggregateId(), command.getAggregateType())) {
      String errorMessage = String.format(
        "The aggregate '%s' with ID '%s' does not exist, which means there is not any account with ID '%s'.",
        command.getAggregateType(),
        command.getAggregateId(),
        command.getAggregateId());
      throw new AggregateNotFoundException(errorMessage);
    }

    // Get the current aggregate.
    Aggregate aggregate = retrieveOrInstantiateAggregate(command.getAggregateId());
    final CreditVO creditVO = ((CreditAccountCommand) command).getCreditVO();

    if (creditVO.isAmountEqualOrLessThanZero()) {
      throw new AmountException("Invalid credit amount: The amount must be greater than zero.");
    }

    try {
      aggregate.applyCommand(command);
      eventStoreService.saveAggregate(aggregate);
      aggregate.markUnconfirmedEventsAsConfirmed();
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new OptimisticConcurrencyException(ex.getMessage(), ex);
    }
    log.info("Successfully handled command {} for aggregate with ID {}", command.getClass().getSimpleName(), command.getAggregateId());
    return aggregate;
  }

  @Nonnull
  @Override
  public Class<CreditAccountCommand> getCommandType() {
    return CreditAccountCommand.class;
  }

  @Nonnull
  @Override
  public AggregateType getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE;
  }
}
