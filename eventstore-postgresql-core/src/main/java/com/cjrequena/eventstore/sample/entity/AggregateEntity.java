package com.cjrequena.eventstore.sample.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "es_aggregate")
public class AggregateEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  @GeneratedValue(generator = "UUID")
  //@GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generates UUIDs in supported databases
  @Column(name = "id", updatable = false, nullable = false) // Enforce not-null and non-updatable
  @JsonProperty(value = "id")
  private UUID id;

  // The aggregate version.
  @Column(name = "aggregate_version", nullable = false)  // Enforce not-null
  @JsonProperty(value = "aggregate_version")
  private long aggregateVersion;

  // The aggregate type.
  @Column(name = "aggregate_type", nullable = false)  // Enforce not-null
  @JsonProperty(value = "aggregate_type")
  private String aggregateType;

}
