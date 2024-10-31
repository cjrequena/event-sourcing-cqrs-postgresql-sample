package com.cjrequena.sample.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "account", schema = "account")
public class AccountEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "owner")
  private String owner;

  @Column(name = "balance")
  private BigDecimal balance;

  @Column(name = "creation_date")
  @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
  private LocalDate creationDate;

  @Column(name = "version")
  private Long version;
}
