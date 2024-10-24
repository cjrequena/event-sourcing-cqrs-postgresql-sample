package com.cjrequena.eventstore.sample.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")  // Equality based on id
@ToString
@Entity
@Table(name = "es_aggregate_snapshot")
public class AggregateSnapshotEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  //@GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generates UUIDs in supported databases
  @GeneratedValue(generator = "UUID")
  @Column(name = "id", updatable = false, nullable = false) // Enforce not-null and non-updatable
  @JsonProperty(value = "id")
  private UUID id;

  // The aggregate id.
  @Column(name = "aggregate_id", nullable = false)
  @JsonProperty(value = "aggregate_id")
  private UUID aggregateId;

  // The aggregate version.
  @Column(name = "aggregate_version", nullable = false)
  @JsonProperty(value = "aggregate_version")
  private long aggregateVersion;

  // The aggregate type.
  @Column(name = "aggregate_type", nullable = false)  // Enforce not-null
  @JsonProperty(value = "aggregate_type")
  private String aggregateType;

  // The actual event data, the event payload.
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "data", columnDefinition = "json", nullable = false)
  @JsonProperty(value = "data")
  private String data;

}
