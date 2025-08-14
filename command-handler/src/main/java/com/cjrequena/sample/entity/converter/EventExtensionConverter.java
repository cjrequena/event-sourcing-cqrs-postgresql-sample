package com.cjrequena.sample.entity.converter;

import com.cjrequena.sample.domain.vo.EventExtensionVO;

public class EventExtensionConverter extends GenericJsonConverter<EventExtensionVO> {
  public EventExtensionConverter() {
    super(EventExtensionVO.class);
  }
}
