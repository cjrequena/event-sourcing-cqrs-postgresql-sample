package com.cjrequena.sample.domain.aggregate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AggregateType {

  ACCOUNT_AGGREGATE("ACCOUNT_AGGREGATE");

  private final String value;

  //private final Class<? extends Aggregate> aggregateClass;

}
