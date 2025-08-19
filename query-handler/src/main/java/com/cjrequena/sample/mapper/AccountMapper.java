package com.cjrequena.sample.mapper;

import com.cjrequena.sample.dto.AccountDTO;
import com.cjrequena.sample.entity.AccountEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
  componentModel = "spring",
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AccountMapper {

  // ========================================
  // AccountDTO  <-> AccountEntity Mappings
  // ========================================

  AccountEntity toEntity(AccountDTO dto);

  AccountDTO toDTO(AccountEntity entity);

  List<AccountEntity> toEntityList(List<AccountDTO> accountDTOList);

  List<AccountDTO> toDTOList(List<AccountEntity> accountEntityList);

  // ========================================
  // Update Mappings
  // ========================================

  //@Mapping(target = "email", source = "email", qualifiedByName = "emailVOToString")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromDTO(AccountDTO accountDTO, @MappingTarget AccountEntity accountEntity);

  //@Mapping(target = "email", source = "email", qualifiedByName = "emailVOToString")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDTOFromEntity(AccountEntity accountEntity, @MappingTarget AccountDTO accountDTO);

}
