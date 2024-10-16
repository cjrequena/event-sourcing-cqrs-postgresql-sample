package com.cjrequena.sample;

import com.cjrequena.sample.common.util.Base64Util;
import com.cjrequena.sample.entity.AccountCreatedEventEntity;
import com.cjrequena.sample.repository.EventRepository;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.EventExtensionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
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
  public void run(String... args) throws JsonProcessingException {
    for (int i = 0; i < 100; i++) {
      AccountVO account = AccountVO.builder().id(UUID.randomUUID()).owner("Carlos").balance(BigDecimal.valueOf(i)).version(1L).build();
      AccountCreatedEventEntity accountCreatedEventEntity = new AccountCreatedEventEntity();
      accountCreatedEventEntity.setEventType("ACCOUNT_CREATED");
      accountCreatedEventEntity.setData(account);
      accountCreatedEventEntity.setDataBase64(Base64Util.objectToJsonBase64(account));
      log.debug(Base64Util.jsonBase64ToObject(Base64Util.objectToJsonBase64(account),AccountVO.class).getClass());
      accountCreatedEventEntity.setEventExtension(EventExtensionVO.builder().traceId(UUID.randomUUID().toString()).build());
      accountCreatedEventEntity.setAggregateId(account.getId());
      accountCreatedEventEntity.setVersion(1L);
      this.eventRepository.save(accountCreatedEventEntity);
    }

    log.debug("GOOD");
  }
}
