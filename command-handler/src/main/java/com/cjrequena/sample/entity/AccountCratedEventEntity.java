package com.cjrequena.sample.entity;

import com.cjrequena.sample.entity.converter.AccountConverter;
import com.cjrequena.sample.entity.converter.EventExtensionConverter;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.EventExtensionVO;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
public class AccountCratedEventEntity extends EventEntity {

  // The actual event data, the event payload.
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "data", columnDefinition = "json")
  @Convert(converter = AccountConverter.class)
  private AccountVO data;

  // // Custom metadata extensions
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "extension", columnDefinition = "json")
  @Convert(converter = EventExtensionConverter.class)
  protected EventExtensionVO eventExtension;
}
