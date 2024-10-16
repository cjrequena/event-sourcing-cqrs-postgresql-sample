package com.cjrequena.sample;

import com.cjrequena.sample.entity.AccountCreatedEventEntity;
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
    for (int i = 0; i < 100; i++) {
      AccountCreatedEventEntity accountCreatedEventEntity = new AccountCreatedEventEntity();
      accountCreatedEventEntity.setEventType("AccountCreated");
      accountCreatedEventEntity.setData(AccountVO.builder().id(UUID.randomUUID()).owner("Carlos").balance(BigDecimal.valueOf(i)).version(1L).build());
      accountCreatedEventEntity.setEventExtension(EventExtensionVO.builder().traceId(UUID.randomUUID().toString()).build());
      accountCreatedEventEntity.setAggregateId(UUID.randomUUID());
      accountCreatedEventEntity.setVersion(1L);
      this.eventRepository.save(accountCreatedEventEntity);
    }

    log.debug("GOOD");
  }
}
