package com.cjrequena.sample.service;

import com.cjrequena.sample.entity.mongo.MongoAccountEntity;
import com.cjrequena.sample.entity.postgresql.AccountEntity;
import com.cjrequena.sample.repository.mongo.MongoAccountRepository;
import com.cjrequena.sample.repository.postgresql.AccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

  private final AccountRepository accountRepository;
  private final MongoAccountRepository mongoAccountRepository;

  /**
   * Save an AccountEntity to the PostgreSQL database.
   *
   * @param entity the account entity to save
   */
  public void save(@Valid @NotNull AccountEntity entity) {
    try {
      log.info("Saving AccountEntity to PostgreSQL: {}", entity);
      accountRepository.save(entity);
      log.info("AccountEntity saved successfully.");
    } catch (Exception ex) {
      log.error("Error while saving AccountEntity to PostgreSQL: {}", entity, ex);
      throw ex; // propagate or handle as needed
    }
  }

  /**
   * Save a MongoAccountEntity to the MongoDB database in a reactive manner.
   *
   * @param entity the Mongo account entity to save
   * @return a Mono indicating completion
   */
  public Mono<MongoAccountEntity> save(@Valid @NotNull MongoAccountEntity entity) {
    log.info("Saving MongoAccountEntity to MongoDB: {}", entity);
    return mongoAccountRepository.save(entity)
      .doOnNext(savedEntity -> log.info("MongoAccountEntity saved successfully: {}", savedEntity))
      .doOnError(ex -> log.error("Error while saving MongoAccountEntity to MongoDB: {}", entity, ex));
  }

}
