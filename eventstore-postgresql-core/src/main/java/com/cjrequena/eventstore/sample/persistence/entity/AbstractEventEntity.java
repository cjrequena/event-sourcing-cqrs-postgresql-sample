package com.cjrequena.eventstore.sample.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id")  // Equality based on id
@ToString
public abstract class AbstractEventEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  @GeneratedValue(generator = "UUID")
  //@GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generates UUIDs in supported databases
  @Column(name = "id", updatable = false, nullable = false) // Enforce not-null and non-updatable
  protected UUID id;

  // The event offset_txid
  @Column(name = "offset_id", insertable = false, updatable = false)
  protected long offsetId;

  // The event offset_txid
  @Column(name = "offset_txid", columnDefinition = "xid8", insertable = false, updatable = false)
  protected long offsetTxId;

  // Unique aggregateId for the specific message. This id is globally unique
  @Column(name = "aggregate_id", nullable = false)
  protected UUID aggregateId;

  // The event version.
  @Column(name = "aggregate_version", nullable = false)
  protected long aggregateVersion;

  // Type of message
  @Column(name = "event_type", nullable = false)
  protected String eventType;

  // The content type of the event data. Must adhere to RFC 2046 format.
  @Column(name = "data_content_type", nullable = false)
  public String dataContentType;

  // Base64 encoded event payload. Must adhere to RFC4648.
  @Column(name = "data_base64", nullable = false)
  protected String dataBase64;

  // A URI describing the schema for the event data
  //protected String dataSchema;

  // The time the event occurred
  @Column(name = "offset_date_time", insertable = false, updatable = false)
  protected OffsetDateTime time;

}
