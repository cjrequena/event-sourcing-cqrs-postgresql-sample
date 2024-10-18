package com.cjrequena.sample.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@Data // Lombok generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok generates a no-args constructor
public abstract class AbstractEventSubscriptionEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "id")
  protected UUID id;

  // The subscription_name
  @Column(name = "subscription_name")
  protected String subscriptionName;

  // The event offset_txid
  @Column(name = "offset_txid")
  protected long offset;

  // The event_id
  @Column(name = "event_id")
  protected UUID eventId;

}
