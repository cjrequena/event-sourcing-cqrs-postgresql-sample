package com.cjrequena.sample;

import com.cjrequena.sample.domain.aggregate.AccountAggregate;
import com.cjrequena.sample.domain.command.*;
import com.cjrequena.sample.domain.event.Event;
import com.cjrequena.sample.exception.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.repository.AggregateRepository;
import com.cjrequena.sample.repository.AggregateSnapshotRepository;
import com.cjrequena.sample.repository.EventRepository;
import com.cjrequena.sample.service.EventStoreService;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.CreditVO;
import com.cjrequena.sample.vo.DebitVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Log4j2
@SpringBootApplication
@RequiredArgsConstructor
public class CommandHandlerMainApplication implements CommandLineRunner {

  private final EventRepository eventRepository;
  private final AggregateRepository aggregateRepository;
  private final AggregateSnapshotRepository aggregateSnapshotRepository;
  private final List<CommandHandler<? extends Command>> commandHandlers;
  private final EventStoreService eventStoreService;

  public static void main(String... args) {
    SpringApplication.run(CommandHandlerMainApplication.class, args);
  }

  @Override
  public void run(String... args) throws JsonProcessingException {

    // Create a new accountVO aggregate
    UUID accountId = UUID.randomUUID();
    AccountAggregate accountAggregate = AccountAggregate.builder().aggregateId(accountId).version(0L).build();

    AccountVO accountVO = AccountVO.builder().id(accountAggregate.getAggregateId()).owner("Carlos").balance(BigDecimal.valueOf(100)).build();

    Command createAccountCommand = CreateAccountCommand.builder()
      .aggregateId(accountAggregate.getAggregateId())
      .accountVO(accountVO)
      .build();

    Command creditAccountCommand = CreditAccountCommand.builder()
      .aggregateId(accountAggregate.getAggregateId())
      .creditVO(CreditVO.builder()
        .accountId(accountId)
        .amount(BigDecimal.valueOf(200))
        .build())
      .build();

    Command debittAccountCommand = DebitAccountCommand.builder()
      .aggregateId(accountAggregate.getAggregateId())
      .debitVO(DebitVO.builder()
        .accountId(accountId)
        .amount(BigDecimal.valueOf(100))
        .build())
      .build();

    accountAggregate.applyCommand(createAccountCommand);
    accountAggregate.applyCommand(creditAccountCommand);
    accountAggregate.applyCommand(creditAccountCommand);
    accountAggregate.applyCommand(creditAccountCommand);
    accountAggregate.applyCommand(debittAccountCommand);

    //    commandHandlers.stream()
    //      .filter(commandHandler -> commandHandler.getCommandType() == command.getClass())
    //      .findFirst()
    //      .ifPresentOrElse(commandHandler -> {
    //        log.info("Handling command {} with {}", command.getClass().getSimpleName(), commandHandler.getClass().getSimpleName());
    //        commandHandler.handle(command, accountAggregate);
    //      }, () -> {
    //        log.info("No specialized handler found with {}", command.getClass().getSimpleName());
    //      });

    // Get the unconfirmed events pool
    List<Event> unconfirmedEventsPool = accountAggregate.getUnconfirmedEventsPool();

    try {
      this.eventStoreService.saveAggregate(accountAggregate);
    } catch (OptimisticConcurrencyServiceException ex) {
      log.error(ex);
    }

    //    List<Event> events = new ArrayList<>(unconfirmedEventsPool);
    //    accountAggregate.markUnconfirmedEventsAsConfirmed();
    // accountAggregate = AccountAggregate.builder().aggregateId(accountId).version(1L).build();
    //accountAggregate.reconstituteFromConfirmedEvents(events);

    log.debug("GOOD");
  }
}
