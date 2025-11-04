package com.cjrequena.sample.persistence.entity.converter;

import com.cjrequena.sample.domain.model.vo.CreditVO;

public class DebitConverter extends GenericJsonConverter<CreditVO> {
  public DebitConverter() {
    super(CreditVO.class);
  }
}
