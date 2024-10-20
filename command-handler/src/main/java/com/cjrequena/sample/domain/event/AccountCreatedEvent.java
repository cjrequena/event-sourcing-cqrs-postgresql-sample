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

  private final AccountVO accountVO;

  @Builder
  public AccountCreatedEvent(
    UUID aggregateId,
    long aggregateVersion,
    String dataContentType,
    String dataBase64,
    AccountVO data
  ){
    super(aggregateId, aggregateVersion);
    this.dataContentType = dataContentType;
    this.dataBase64 = dataBase64;
    this.accountVO = data;
  }

  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_CREATED_EVENT.toString();
  }

}
