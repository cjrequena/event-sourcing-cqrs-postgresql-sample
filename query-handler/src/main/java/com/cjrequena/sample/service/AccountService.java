package com.cjrequena.sample.service;

import com.cjrequena.sample.dto.AccountDTO;
import com.cjrequena.sample.entity.AccountEntity;
import com.cjrequena.sample.exception.service.AccountNotFoundServiceException;
import com.cjrequena.sample.mapper.AccountMapper;
import com.cjrequena.sample.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;

  @SneakyThrows
  public AccountDTO retrieveById(UUID accountId) {
    //--
    Optional<AccountEntity> entity = this.accountRepository.findById(accountId);
    if (entity.isEmpty()) {
      log.error("Account {} does not exist", accountId);
      throw new AccountNotFoundServiceException("Bank account {} does not exist");
    }
    return accountMapper.mapToDTO(entity.get());
    //--
  }

  public List<AccountDTO> retrieve() {
    //--
    try {
      final List<AccountEntity> bankAccountEntities = Streamable.of(this.accountRepository.findAll()).toList();
      return bankAccountEntities.stream().map(accountMapper::mapToDTO).collect(Collectors.toList());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    //--
  }

}
