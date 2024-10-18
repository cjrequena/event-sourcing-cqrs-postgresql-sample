package com.cjrequena.sample.domain.event;

import com.cjrequena.sample.vo.AccountVO;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class AccountCreatedEvent extends Event{

  private final AccountVO account;

  @Builder
  public AccountCreatedEvent(UUID aggregateId, long aggregateVersion, AccountVO account){
    super(aggregateId, aggregateVersion);
    this.account = account;
  }

  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_CREATED_EVENT.toString();
  }

}
