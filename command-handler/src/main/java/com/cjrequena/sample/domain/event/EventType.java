package com.cjrequena.sample.domain.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType {

    ACCOUNT_CREATED_EVENT(AccountCreatedEvent.class),
    ACCOUNT_CREDITED_EVENT(AccountCreditedEvent.class),
    ACCOUNT_DEBITED_EVENT(AccountDebitedEvent.class);

    private final Class<? extends Event> eventClass;

    //private final String value;

}
