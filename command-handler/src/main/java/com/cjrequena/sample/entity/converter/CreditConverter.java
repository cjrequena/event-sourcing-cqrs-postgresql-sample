package com.cjrequena.sample.entity.converter;

import com.cjrequena.sample.domain.vo.CreditVO;

public class CreditConverter extends GenericJsonConverter<CreditVO> {
  public CreditConverter() {
    super(CreditVO.class);
  }
}
