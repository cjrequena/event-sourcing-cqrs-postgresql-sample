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
public abstract class AbstractAggregateEntity {

  // Unique id for the specific message. This id is globally unique
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "id")
  private UUID id;

  // The aggregate version.
  @Column(name = "aggregate_version")
  private long aggregateVersion;

  // The aggregate type.
  @Column(name = "aggregate_type")
  private String aggregateType;

}
