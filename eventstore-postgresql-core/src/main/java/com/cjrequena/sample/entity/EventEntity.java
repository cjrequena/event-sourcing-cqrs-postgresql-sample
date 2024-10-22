package com.cjrequena.sample.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "es_event")
public class EventEntity extends AbstractEventEntity {

  // The actual event data, the event payload.
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "data", columnDefinition = "json")
  private String data;

  // // Custom metadata extensions
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "extension", columnDefinition = "json")
  protected String extension;
}
