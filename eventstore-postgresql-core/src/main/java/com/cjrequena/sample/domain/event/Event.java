package com.cjrequena.sample.domain.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public abstract class Event {

    // Unique id for the specific message. This id is globally unique
    protected UUID id;

    // The event offset_txid
    protected long offset;

    // Unique aggregateId for the specific message. This id is globally unique
    protected final UUID aggregateId;

    // The event aggregateVersion.
    protected final long aggregateVersion;

    // Type of message
    protected String eventType;

    // The content type of the event data. Must adhere to RFC 2046 format.
    public String dataContentType;

    // Base64 encoded event payload. Must adhere to RFC4648.
    protected String dataBase64;

    // A URI describing the schema for the event data
    //protected String dataSchema;

    // The time the event occurred
    protected OffsetDateTime time;;

}
