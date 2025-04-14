package com.cjrequena.sample.entity.mongo;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "account")
public class MongoAccountEntity {

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Field
  private String owner;

  @Field
  private BigDecimal balance;

  @Field
  private LocalDate creationDate;

  @Field
  private Long version;
}
