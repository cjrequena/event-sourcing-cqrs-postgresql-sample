package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.domain.command.CreditAccountCommand;
import com.cjrequena.sample.exception.service.AggregateNotFoundServiceException;
import com.cjrequena.sample.exception.service.AmountServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.vo.CreditVO;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Log4j2
@Component
@Transactional
public class CreditAccountCommandHandler extends CommandHandler<CreditAccountCommand> {

  @Autowired
  public CreditAccountCommandHandler(
    EventStoreService eventStoreService,
    AggregateFactory aggregateFactory,
    EventMapper eventMapper,
    EventStoreConfigurationProperties eventStoreConfigurationProperties) {
    super(eventStoreService, aggregateFactory, eventMapper, eventStoreConfigurationProperties);
  }

  @Override
  public void handle(@Nonnull Command command) {
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
      throw new AggregateNotFoundServiceException(errorMessage);
    }

    final CreditVO creditVO = ((CreditAccountCommand) command).getCreditVO();
    if (creditVO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new AmountServiceException("Invalid credit amount: The amount must be greater than zero.");
    }

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
  public Class<CreditAccountCommand> getCommandType() {
    return CreditAccountCommand.class;
  }

  @Nonnull
  @Override
  public AggregateType getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE;
  }
}
