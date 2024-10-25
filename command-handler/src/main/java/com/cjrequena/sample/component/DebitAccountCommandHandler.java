package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
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
  public void handle(Command command, Aggregate aggregate) {
    log.trace("Handling {} with command {}", CreateAccountCommand.class.getSimpleName(), command);
    final DebitVO debitVO = ((DebitAccountCommand) command).getDebitVO();
    if (debitVO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new AmountServiceException("Invalid debit amount: The amount must be greater than zero.");
    }
    aggregate.applyCommand(command);
    try {
      eventStoreService.saveAggregate(aggregate);
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new OptimisticConcurrencyServiceException(ex.getMessage(), ex);
    }
    aggregate.markUnconfirmedEventsAsConfirmed();
  }

  @Nonnull
  @Override
  public Class<DebitAccountCommand> getCommandType() {
    return DebitAccountCommand.class;
  }


}
