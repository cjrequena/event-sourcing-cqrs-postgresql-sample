package com.cjrequena.sample.repository.mongo;

import com.cjrequena.sample.entity.mongo.MongoAccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface MongoAccountRepository extends ReactiveMongoRepository<MongoAccountEntity, UUID> {

}
