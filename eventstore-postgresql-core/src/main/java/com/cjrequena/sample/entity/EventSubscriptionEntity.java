package com.cjrequena.sample.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")  // Equality based on id
@ToString
@Entity
@Table(name = "es_event_subscription")
public class EventSubscriptionEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  //@GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generates UUIDs in supported databases
  @GeneratedValue(generator = "UUID")
  @Column(name = "id", updatable = false, nullable = false) // Enforce not-null and non-updatable
  protected UUID id;

  // The subscription_name
  @Column(name = "subscription_name", nullable = false)
  protected String subscriptionName;

  // The event offset_txid
  @Column(name = "offset_txid", nullable = false)
  protected long offsetTxId;

  // The event_id
  @Column(name = "event_id", nullable = false)
  protected UUID eventId;

}
