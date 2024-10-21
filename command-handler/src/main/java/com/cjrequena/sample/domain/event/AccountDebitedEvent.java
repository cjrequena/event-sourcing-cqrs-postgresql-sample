package com.cjrequena.sample.domain.event;

import com.cjrequena.sample.entity.AccountDebitedEventEntity;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.vo.DebitVO;
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
public class AccountDebitedEvent extends Event{

  private final DebitVO data;
  private final EventExtensionVO extension;

  @Builder
  public AccountDebitedEvent(
    UUID aggregateId,
    long aggregateVersion,
    DebitVO data,
    String dataBase64,
    String dataContentType,
    EventExtensionVO extension
  ){
    super(aggregateId, aggregateVersion);
    this.data = data;
    this.dataBase64 = dataBase64;
    this.dataContentType = dataContentType;
    this.extension = extension;
  }

  public AccountDebitedEventEntity mapToEventEntity() {
    log.info("Appending event {}", this);
    return EventMapper.INSTANCE.toEventEntity(this);
  }

  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_DEBITED_EVENT.toString();
  }

}
