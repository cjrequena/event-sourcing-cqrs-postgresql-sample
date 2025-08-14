package com.cjrequena.sample.entity.converter;

import com.cjrequena.sample.domain.vo.CreditVO;

public class DebitConverter extends GenericJsonConverter<CreditVO> {
  public DebitConverter() {
    super(CreditVO.class);
  }
}
