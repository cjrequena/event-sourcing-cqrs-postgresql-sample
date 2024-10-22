package com.cjrequena.sample.domain.event;

import com.cjrequena.sample.entity.EventEntity;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.vo.CreditVO;
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
public class AccountCreditedEvent extends Event{

  private final CreditVO data;
  private final EventExtensionVO extension;

  public EventEntity mapToEventEntity() {
    log.info("Appending event {}", this);
    return EventMapper.INSTANCE.toEventEntity(this);
  }


  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_CREDITED_EVENT.toString();
  }

}
