package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.command.DebitAccountCommand;
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
    if (!(command instanceof DebitAccountCommand)) {
      throw new IllegalArgumentException("Expected command of type DebitAccountCommand but received " + command.getClass().getSimpleName());
    }

    log.trace("Handling command of type {} for aggregate {}", command.getClass().getSimpleName(), aggregate.getClass().getSimpleName());

    final DebitVO debitVO = ((DebitAccountCommand) command).getDebitVO();
    if (debitVO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new AmountServiceException("Invalid debit amount: The amount must be greater than zero.");
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
