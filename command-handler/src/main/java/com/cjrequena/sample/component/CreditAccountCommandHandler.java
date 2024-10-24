package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.domain.command.CreditAccountCommand;
import com.cjrequena.sample.exception.service.AmountServiceException;
import com.cjrequena.sample.vo.CreditVO;
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
public class CreditAccountCommandHandler implements CommandHandler<CreditAccountCommand> {

  private final EventStoreService eventStoreService;


  @Override
  public void handle(Command command, Aggregate aggregate) {
    log.trace("Handling {} with command {}", CreateAccountCommand.class.getSimpleName(), command);
    final CreditVO creditVO = ((CreditAccountCommand) command).getCreditVO();
    if (creditVO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new AmountServiceException("Invalid credit amount: The amount must be greater than zero.");
    }
    aggregate.applyCommand(command);
    eventStoreService.saveAggregate(aggregate);
    aggregate.markUnconfirmedEventsAsConfirmed();
  }

  @Nonnull
  @Override
  public Class<CreditAccountCommand> getCommandType() {
    return CreditAccountCommand.class;
  }


}
