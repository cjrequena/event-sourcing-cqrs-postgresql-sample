package com.cjrequena.sample.domain.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType {

    ACCOUNT_CREATED_EVENT("ACCOUNT_CREATED_EVENT");

    private final String value;

    //private final Class<? extends AbstractEvent> eventClass;
}
