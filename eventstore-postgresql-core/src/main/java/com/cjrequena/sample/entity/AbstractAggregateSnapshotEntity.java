package com.cjrequena.sample.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@MappedSuperclass
@Data // Lombok generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok generates a no-args constructor
public abstract class AbstractAggregateSnapshotEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "id")
  private UUID id;

  // The aggregate id.
  @Column(name = "aggregate_id")
  private UUID aggregateId;

  // The aggregate version.
  @Column(name = "aggregate_version")
  private Long aggregateVersion;

  // The actual event data, the event payload.
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "data", columnDefinition = "json")
  private String data;

  // Base64 encoded event payload. Must adhere to RFC4648.
  @Column(name = "data_base64")
  protected String dataBase64;

}
