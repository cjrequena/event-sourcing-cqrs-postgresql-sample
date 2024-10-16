package com.cjrequena.sample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "es_aggregate")
public class AggregateEntity extends AbstractAggregateEntity {

}
