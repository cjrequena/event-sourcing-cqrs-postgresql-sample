package com.cjrequena.sample.persistence.entity.postgresql;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account", schema = "account")
public class AccountEntity {

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "owner", nullable = false)
  private String owner;

  @Column(name = "balance", nullable = false)
  private BigDecimal balance;

  @Column(name = "creation_date", insertable = false, updatable = false)
  @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
  private LocalDate creationDate;

  @Column(name = "version", nullable = false)
  private Long version;
}
