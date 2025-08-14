package com.cjrequena.sample.mapper;

import com.cjrequena.sample.domain.vo.AccountVO;
import com.cjrequena.sample.dto.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
  componentModel = "spring",
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AccountMapper {

  AccountVO toAccountVO(AccountDTO accountDTO);
}
