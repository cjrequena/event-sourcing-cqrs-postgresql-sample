package com.cjrequena.sample.domain.aggregate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EAggregateType {

  ACCOUNT_AGGREGATE("ACCOUNT_AGGREGATE");

  private final String value;

}
