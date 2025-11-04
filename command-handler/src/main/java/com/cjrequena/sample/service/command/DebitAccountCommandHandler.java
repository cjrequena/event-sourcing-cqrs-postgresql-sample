package com.cjrequena.sample.service.command;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.exception.AccountBalanceException;
import com.cjrequena.sample.domain.exception.AggregateNotFoundException;
import com.cjrequena.sample.domain.exception.AmountException;
import com.cjrequena.sample.domain.exception.OptimisticConcurrencyException;
import com.cjrequena.sample.domain.mapper.EventMapper;
import com.cjrequena.sample.domain.model.aggregate.Account;
import com.cjrequena.sample.domain.model.aggregate.AggregateType;
import com.cjrequena.sample.domain.model.command.DebitAccountCommand;
import com.cjrequena.sample.domain.model.vo.DebitVO;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@Transactional
public class DebitAccountCommandHandler extends CommandHandler<DebitAccountCommand> {

  @Autowired
  public DebitAccountCommandHandler(
    EventStoreService eventStoreService,
    AggregateFactory aggregateFactory,
    EventMapper eventMapper,
    EventStoreConfigurationProperties eventStoreConfigurationProperties) {
    super(eventStoreService, aggregateFactory, eventMapper, eventStoreConfigurationProperties);
  }

  @Override
  public Aggregate handle(@Nonnull Command command) {
    log.trace("Handling command of type {} for aggregate {}", command.getClass().getSimpleName(), command.getAggregateType());

    if (!(command instanceof DebitAccountCommand)) {
      throw new IllegalArgumentException("Expected command of type DebitAccountCommand but received " + command.getClass().getSimpleName());
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
    final Account account = ((Account) aggregate);
    final DebitVO debitVO = ((DebitAccountCommand) command).getDebitVO();

    if (debitVO.isAmountEqualOrLessThanZero()) {
      throw new AmountException("Invalid debit amount: The amount must be greater than zero.");
    }

    if (!account.hasSufficientBalance(debitVO.amount())) {
      String errorMessage = String.format("Invalid account balance for account with ID '%s': Balance cannot be negative.", account.getId());
      throw new AccountBalanceException(errorMessage);
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
  public Class<DebitAccountCommand> getCommandType() {
    return DebitAccountCommand.class;
  }

  @Nonnull
  @Override
  public AggregateType getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE;
  }
}
