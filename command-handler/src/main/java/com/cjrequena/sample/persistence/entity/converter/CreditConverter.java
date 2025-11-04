package com.cjrequena.sample.persistence.entity.converter;

import com.cjrequena.sample.domain.model.vo.CreditVO;

public class CreditConverter extends GenericJsonConverter<CreditVO> {
  public CreditConverter() {
    super(CreditVO.class);
  }
}
