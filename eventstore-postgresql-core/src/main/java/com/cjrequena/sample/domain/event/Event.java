package com.cjrequena.sample.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class Event<T> {

    // Unique id for the specific message. This id is globally unique
    protected UUID id;

    // The event offset_txid
    protected Long offset;

    // Unique aggregateId for the specific message. This id is globally unique
    protected UUID aggregateId;

    // The event version.
    protected Long version;

    // Type of message
    protected String eventType;

    // The content type of the event data. Must adhere to RFC 2046 format.
    public String dataContentType;

    // The actual event data, the event payload.
    protected T data;

    // Base64 encoded event payload. Must adhere to RFC4648.
    protected String dataBase64;

    // A URI describing the schema for the event data
    //protected String dataSchema;

    // The time the event occurred
    protected OffsetDateTime offsetDateTime;

}
