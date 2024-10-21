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
@Table(name = "es_aggregate")
public  class AggregateEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  @GeneratedValue(generator = "UUID")
  //@GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generates UUIDs in supported databases
  @Column(name = "id", updatable = false, nullable = false) // Enforce not-null and non-updatable
  private UUID id;

  // The aggregate version.
  @Column(name = "aggregate_version", nullable = false)  // Enforce not-null
  private long aggregateVersion;

  // The aggregate type.
  @Column(name = "aggregate_type", nullable = false)  // Enforce not-null
  private String aggregateType;

}
