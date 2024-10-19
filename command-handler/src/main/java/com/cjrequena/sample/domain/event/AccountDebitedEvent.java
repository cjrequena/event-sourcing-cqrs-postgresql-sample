package com.cjrequena.sample.domain.event;

import com.cjrequena.sample.vo.DebitVO;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class AccountDebitedEvent extends Event{

  private final DebitVO credit;

  @Builder
  public AccountDebitedEvent(
    UUID aggregateId,
    long aggregateVersion,
    String dataContentType,
    String dataBase64,
    DebitVO credit
  ){
    super(aggregateId, aggregateVersion);
    this.credit = credit;
  }

  @Nonnull
  @Override
  public String getEventType() {
    return EventType.ACCOUNT_CREATED_EVENT.toString();
  }

}
