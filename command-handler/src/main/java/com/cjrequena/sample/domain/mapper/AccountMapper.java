package com.cjrequena.sample.domain.mapper;

import com.cjrequena.sample.controller.dto.AccountDTO;
import com.cjrequena.sample.domain.model.vo.AccountVO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
  componentModel = "spring",
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AccountMapper {

  AccountVO toAccountVO(AccountDTO accountDTO);
}
