package com.cjrequena.sample.domain.event;

import com.cjrequena.sample.entity.AccountCreatedEventEntity;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.EventExtensionVO;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
@Log4j2
public class AccountCreatedEvent extends Event {

  private final AccountVO data;
  private final EventExtensionVO extension;

  @Builder
  public AccountCreatedEvent(
    UUID aggregateId,
    long aggregateVersion,
    String dataContentType,
    String dataBase64,
    AccountVO data,
    EventExtensionVO extension
  ) {
    super(aggregateId, aggregateVersion);
    this.dataContentType = dataContentType;
    this.dataBase64 = dataBase64;
    this.data = data;
    this.extension = extension;
  }

  public AccountCreatedEventEntity mapToEventEntity() {
    log.info("Appending event {}", this);
    return EventMapper.INSTANCE.toEventEntity(this);
  }

  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_CREATED_EVENT.toString();
  }

}
