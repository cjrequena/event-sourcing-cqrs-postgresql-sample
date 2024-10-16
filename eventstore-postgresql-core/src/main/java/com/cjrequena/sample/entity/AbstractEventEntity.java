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
public abstract class AbstractEventEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "id")
  protected UUID id;

  // The event offset_txid
  @Column(name = "offset_txid", columnDefinition = "xid8", insertable = false, updatable = false)
  protected Long offset;

  // Unique aggregateId for the specific message. This id is globally unique
  @Column(name = "aggregate_id")
  protected UUID aggregateId;

  // The event version.
  @Column(name = "version")
  protected Long version;

  // Type of message
  @Column(name = "event_type")
  protected String eventType;

  // The content type of the event data. Must adhere to RFC 2046 format.
  @Column(name = "data_content_type")
  public String dataContentType;

  // Base64 encoded event payload. Must adhere to RFC4648.
  @Column(name = "data_base64")
  protected String dataBase64;

  // A URI describing the schema for the event data
  //protected String dataSchema;

  // The time the event occurred
  //    @Column(name = "offset_date_time")
  //    protected OffsetDateTime offsetDateTime;

}
