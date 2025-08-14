package com.cjrequena.sample.entity.converter;

import com.cjrequena.sample.domain.vo.AccountVO;

public class AccountConverter extends GenericJsonConverter<AccountVO> {
  public AccountConverter() {
    super(AccountVO.class);
  }
}
