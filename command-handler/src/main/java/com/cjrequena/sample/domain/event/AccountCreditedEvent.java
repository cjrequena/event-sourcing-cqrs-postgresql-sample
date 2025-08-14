package com.cjrequena.sample.domain.event;

import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.sample.common.util.ApplicationContextProvider;
import com.cjrequena.sample.domain.vo.CreditVO;
import com.cjrequena.sample.domain.vo.EventExtensionVO;
import com.cjrequena.sample.mapper.EventMapper;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@Log4j2
public class AccountCreditedEvent extends Event {

  private final CreditVO data;
  private final EventExtensionVO extension;
  private final EventMapper eventMapper = ApplicationContextProvider.getContext().getBean(EventMapper.class);

  public EventEntity mapToEventEntity() {
    return this.eventMapper.mapToEventEntity(this);
  }

  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_CREDITED_EVENT.getEventType();
  }

}
