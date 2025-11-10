package com.cjrequena.sample.persistence.entity;

import com.cjrequena.eventstore.sample.persistence.entity.AbstractEventEntity;
import com.cjrequena.sample.domain.model.vo.DebitVO;
import com.cjrequena.sample.domain.model.vo.EventExtensionVO;
import com.cjrequena.sample.persistence.entity.converter.DebitConverter;
import com.cjrequena.sample.persistence.entity.converter.EventExtensionConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "es_event")
public class AccountDebitedEventEntity extends AbstractEventEntity {

  // The actual event data, the event payload.
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "data", columnDefinition = "json")
  @Convert(converter = DebitConverter.class)
  private DebitVO data;

  // // Custom metadata extensions
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "extension", columnDefinition = "json")
  @Convert(converter = EventExtensionConverter.class)
  protected EventExtensionVO extension;
}
