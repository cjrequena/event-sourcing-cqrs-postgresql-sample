package com.cjrequena.sample.persistence.entity.converter;

import com.cjrequena.sample.domain.model.vo.EventExtensionVO;

public class EventExtensionConverter extends GenericJsonConverter<EventExtensionVO> {
  public EventExtensionConverter() {
    super(EventExtensionVO.class);
  }
}
