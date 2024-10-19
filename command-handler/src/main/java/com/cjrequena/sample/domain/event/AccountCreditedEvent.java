package com.cjrequena.sample.domain.event;

import com.cjrequena.sample.vo.CreditVO;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class AccountCreditedEvent extends Event{

  private final CreditVO credit;

  @Builder
  public AccountCreditedEvent(
    UUID aggregateId,
    long aggregateVersion,
    String dataContentType,
    String dataBase64,
    CreditVO credit
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
