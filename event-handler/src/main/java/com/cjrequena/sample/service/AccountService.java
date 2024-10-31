package com.cjrequena.sample.service;

import com.cjrequena.sample.entity.AccountEntity;
import com.cjrequena.sample.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

  private final AccountRepository accountRepository;


  @SneakyThrows
  public void save(AccountEntity entity) {
    //--
    this.accountRepository.save(entity);
    //--
  }


}
