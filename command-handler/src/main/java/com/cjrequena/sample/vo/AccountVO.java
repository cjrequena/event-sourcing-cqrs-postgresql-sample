package com.cjrequena.sample.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data // Lombok generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok generates a no-args constructor
@AllArgsConstructor // Lombok generates an all-args constructor
@Builder
public class AccountVO implements Serializable {

  private UUID id;

  private String owner;

  private BigDecimal balance;

  private Long version;

}
