package com.cjrequena.eventstore.sample.domain.event;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
@ToString
@JsonPropertyOrder(value = {
  "id",
  "subscription_name",
  "offset_txid",
  "offset_id",
  "event_id"
})
public class EventSubscription implements Serializable {

  // Unique id. This id is globally unique
  protected UUID id;

  // The subscription_name
  protected String subscriptionName;

  // The event offset_txid
  protected long offsetTxId;

  protected long offsetId;

  // The event_id
  protected UUID eventId;

  // No setters to maintain immutability

}
