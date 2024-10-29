package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.mapper.EventMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
@Log4j2
public class AccountEventHandler implements AsyncEventHandler{

  private final EventMapper eventMapper;
  
  @Override
  public void handle(EventEntity eventEntity) {
    this.eventMapper.mapToEvent(eventEntity);

    log.info(getAggregateType());
    //log.info(event);
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.getAggregateType();
  }

}
