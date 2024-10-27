package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.aggregate.Account;
import com.cjrequena.sample.domain.aggregate.AccountAggregate;
import com.cjrequena.sample.domain.command.DebitAccountCommand;
import com.cjrequena.sample.exception.service.AccountBalanceServiceException;
import com.cjrequena.sample.exception.service.AggregateNotFoundServiceException;
import com.cjrequena.sample.exception.service.AmountServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.vo.DebitVO;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class DebitAccountCommandHandler implements CommandHandler<DebitAccountCommand> {

  private final EventStoreService eventStoreService;

  @Override
  public void handle(@Nonnull Command command, @Nonnull Aggregate aggregate) {

    log.trace("Handling command of type {} for aggregate {}", command.getClass().getSimpleName(), aggregate.getClass().getSimpleName());

    if (!(command instanceof DebitAccountCommand)) {
      throw new IllegalArgumentException("Expected command of type DebitAccountCommand but received " + command.getClass().getSimpleName());
    }

    final Account account = ((AccountAggregate) aggregate).getAccount();
    final DebitVO debitVO = ((DebitAccountCommand) command).getDebitVO();

    if (!this.eventStoreService.verifyIfAggregateExist(aggregate.getAggregateId(), aggregate.getAggregateType())) {
      String errorMessage = String.format(
        "The aggregate '%s' with ID '%s' does not exist, which means there is not any account with ID '%s'.",
        aggregate.getAggregateType(),
        aggregate.getAggregateId(),
        aggregate.getAggregateId());
      throw new AggregateNotFoundServiceException(errorMessage);
    }

    if (debitVO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new AmountServiceException("Invalid debit amount: The amount must be greater than zero.");
    }

    if (account.getBalance().subtract(debitVO.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
      String errorMessage = String.format("Invalid account balance for account with ID '%s': Balance cannot be negative.", account.getId());
      throw new AccountBalanceServiceException(errorMessage);
    }

    try {
      aggregate.applyCommand(command);
      eventStoreService.saveAggregate(aggregate);
      aggregate.markUnconfirmedEventsAsConfirmed();
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new OptimisticConcurrencyServiceException(ex.getMessage(), ex);
    }

    log.info("Successfully handled command {} and updated aggregate with ID {}", command.getClass().getSimpleName(), aggregate.getAggregateId());

  }

  @Nonnull
  @Override
  public Class<DebitAccountCommand> getCommandType() {
    return DebitAccountCommand.class;
  }

}
