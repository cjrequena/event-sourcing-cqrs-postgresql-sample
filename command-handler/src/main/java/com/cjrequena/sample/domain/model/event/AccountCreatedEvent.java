package com.cjrequena.sample.domain.model.event;

import com.cjrequena.eventstore.sample.domain.model.event.Event;
import com.cjrequena.eventstore.sample.persistence.entity.EventEntity;
import com.cjrequena.sample.domain.mapper.EventMapper;
import com.cjrequena.sample.domain.model.vo.AccountVO;
import com.cjrequena.sample.domain.model.vo.EventExtensionVO;
import com.cjrequena.sample.shared.common.util.ApplicationContextProvider;
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
  private final EventMapper eventMapper = ApplicationContextProvider.getContext().getBean(EventMapper.class);

  public EventEntity mapToEventEntity() {
    return this.eventMapper.mapToEventEntity(this);
  }

  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_CREATED_EVENT.getEventType();
  }

}
