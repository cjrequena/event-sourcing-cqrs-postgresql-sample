package com.cjrequena.sample;

import com.cjrequena.sample.common.util.Base64Util;
import com.cjrequena.sample.domain.aggregate.AccountAggregate;
import com.cjrequena.sample.domain.command.Command;
import com.cjrequena.sample.domain.command.CommandHandler;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.domain.event.AccountCreatedEvent;
import com.cjrequena.sample.domain.event.Event;
import com.cjrequena.sample.entity.AccountCreatedEventEntity;
import com.cjrequena.sample.entity.AggregateEntity;
import com.cjrequena.sample.entity.AggregateSnapshotEntity;
import com.cjrequena.sample.repository.AggregateRepository;
import com.cjrequena.sample.repository.AggregateSnapshotRepository;
import com.cjrequena.sample.repository.EventRepository;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.EventExtensionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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


  public static void main(String... args) {
    SpringApplication.run(CommandHandlerMainApplication.class, args);
  }

  @Override
  public void run(String... args) throws JsonProcessingException {



    for (int i = 1; i <= 100; i++) {




      AggregateEntity aggregateEntity = new AggregateEntity();
      aggregateEntity.setAggregateType("ACCOUNT_AGGREGATE");
      aggregateEntity.setAggregateVersion(1L);

      this.aggregateRepository.save(aggregateEntity);
      AccountVO account = AccountVO.builder().id(aggregateEntity.getId()).owner("Carlos").balance(BigDecimal.valueOf(i)).version(1L).build();
      AccountCreatedEventEntity accountCreatedEventEntity = new AccountCreatedEventEntity();
      accountCreatedEventEntity.setAggregateId(account.getId());
      accountCreatedEventEntity.setEventType("ACCOUNT_CREATED");
      accountCreatedEventEntity.setData(account);
      accountCreatedEventEntity.setDataBase64(Base64Util.objectToJsonBase64(account));
      log.debug(Base64Util.jsonBase64ToObject(Base64Util.objectToJsonBase64(account),AccountVO.class).getClass());
      accountCreatedEventEntity.setEventExtension(EventExtensionVO.builder().traceId(UUID.randomUUID().toString()).build());
      accountCreatedEventEntity.setAggregateVersion(1L);
      accountCreatedEventEntity.setDataContentType("application/json");
      this.eventRepository.save(accountCreatedEventEntity);


      if(i % 10==0){
        AggregateSnapshotEntity aggregateSnapshotEntity = new AggregateSnapshotEntity();
        aggregateSnapshotEntity.setAggregateId(aggregateEntity.getId());
        aggregateSnapshotEntity.setAggregateVersion(aggregateEntity.getAggregateVersion());
        aggregateSnapshotEntity.setData(Base64Util.objectToJsonString(account));
        aggregateSnapshotEntity.setDataBase64(Base64Util.objectToJsonBase64(account));
        this.aggregateSnapshotRepository.save(aggregateSnapshotEntity);
      }

      Command command = new CreateAccountCommand(account);

      commandHandlers.stream()
        .filter(commandHandler -> commandHandler.getCommandType() == command.getClass())
        .findFirst()
        .ifPresentOrElse(commandHandler -> {
          log.info("Handling command {} with {}", command.getClass().getSimpleName(), commandHandler.getClass().getSimpleName());
          commandHandler.handle();
        }, () -> {
          log.info("No specialized handler found with {}", command.getClass().getSimpleName());
        });

      AccountAggregate accountAggregate = new AccountAggregate(accountCreatedEventEntity.getAggregateId(),0);
      Event event = AccountCreatedEvent.builder()
        .aggregateId(accountCreatedEventEntity.getAggregateId())
        .aggregateVersion(accountCreatedEventEntity.getAggregateVersion())
        .account(accountCreatedEventEntity.getData())
        .dataBase64(accountCreatedEventEntity.getDataBase64())
        .dataContentType(accountCreatedEventEntity.dataContentType)
        .build();
      List<Event> events = new ArrayList<>(Collections.singletonList(event));

      accountAggregate.reconstituteFromConfirmedEvents(events);
      log.debug(accountAggregate);

    }




    log.debug("GOOD");
  }
}
