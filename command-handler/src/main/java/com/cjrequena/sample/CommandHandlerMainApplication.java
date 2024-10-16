package com.cjrequena.sample;

import com.cjrequena.sample.entity.AccountCratedEventEntity;
import com.cjrequena.sample.repository.EventRepository;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.EventExtensionVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.UUID;

@Log4j2
@SpringBootApplication
public class CommandHandlerMainApplication implements CommandLineRunner {

  @Autowired
  private EventRepository eventRepository;

  public static void main(String... args) {
    SpringApplication.run(CommandHandlerMainApplication.class, args);
  }

  @Override
  public void run(String... args) {
    for (int i = 0; i < 99; i++) {
      AccountCratedEventEntity accountCratedEventEntity = new AccountCratedEventEntity();
      accountCratedEventEntity.setEventType("AccountCreated");
      accountCratedEventEntity.setData(AccountVO.builder().id(UUID.randomUUID()).owner("Carlos").balance(BigDecimal.valueOf(100)).version(1L).build());
      accountCratedEventEntity.setEventExtension(EventExtensionVO.builder().traceId(UUID.randomUUID().toString()).build());
      accountCratedEventEntity.setAggregateId(UUID.randomUUID());
      accountCratedEventEntity.setVersion(1L);
      this.eventRepository.save(accountCratedEventEntity);
    }

    log.debug("GOOD");
  }
}
