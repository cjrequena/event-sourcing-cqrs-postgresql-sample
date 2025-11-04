package com.cjrequena.sample.persistence.entity.converter;

import com.cjrequena.sample.domain.model.vo.AccountVO;

public class AccountConverter extends GenericJsonConverter<AccountVO> {
  public AccountConverter() {
    super(AccountVO.class);
  }
}
