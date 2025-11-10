package com.cjrequena.sample.domain.model.aggregate;

import com.cjrequena.eventstore.sample.domain.model.aggregate.Aggregate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AggregateType {

  ACCOUNT_AGGREGATE(Account.class, "ACCOUNT_AGGREGATE");

  private final Class<? extends Aggregate> clazz;
  private final String type;
  //private final String value;

}
