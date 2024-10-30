package com.cjrequena.sample.repository;

import com.cjrequena.sample.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, UUID>, QueryByExampleExecutor<AccountEntity> {

}
