package com.cjrequena.sample.domain.event;

import com.cjrequena.eventstore.sample.domain.event.Event;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType {

  ACCOUNT_CREATED_EVENT(AccountCreatedEvent.class, AccountCreatedEvent.class.getName()),
  ACCOUNT_CREDITED_EVENT(AccountCreditedEvent.class, AccountCreditedEvent.class.getName()),
  ACCOUNT_DEBITED_EVENT(AccountDebitedEvent.class, AccountDebitedEvent.class.getName());

  private final Class<? extends Event> eventClass;
  private final String eventType;

  //private final String value;
}
