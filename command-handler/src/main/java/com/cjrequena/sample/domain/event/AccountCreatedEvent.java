package com.cjrequena.sample.domain.event;

import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.EventExtensionVO;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@Log4j2
public class AccountCreatedEvent extends Event {

  private final AccountVO data;
  private final EventExtensionVO extension;

  public EventEntity mapToEventEntity() {
    log.info("Appending event {}", this);
    return EventMapper.INSTANCE.mapToEventEntity(this);
  }

  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_CREATED_EVENT.getEventType();
  }

}
