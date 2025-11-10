package com.cjrequena.eventstore.sample.persistence.repository;

import com.cjrequena.eventstore.sample.persistence.entity.EventSubscriptionEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface EventSubscriptionRepository extends CrudRepository<EventSubscriptionEntity, UUID> {

  @Modifying
  @Query(value = """
    INSERT INTO ES_EVENT_SUBSCRIPTION (SUBSCRIPTION_NAME, OFFSET_TXID, OFFSET_ID)
    VALUES (:subscriptionName, '0'::xid8, 0)
    ON CONFLICT DO NOTHING
    """, nativeQuery = true)
  void registerNewSubscriptionIfAbsent(@Param("subscriptionName") String subscriptionName);

  @Query(value = """
    SELECT ID, SUBSCRIPTION_NAME, OFFSET_TXID, OFFSET_ID
      FROM ES_EVENT_SUBSCRIPTION
     WHERE SUBSCRIPTION_NAME = :subscriptionName
       FOR UPDATE SKIP LOCKED
    """,
    nativeQuery = true)
  Optional<EventSubscriptionEntity> retrieveEventSubscriptionAndLockSubscriptionOffset(@Param("subscriptionName") @Nonnull String subscriptionName);

  @Modifying
  @Query(value = """
    UPDATE ES_EVENT_SUBSCRIPTION
    SET OFFSET_TXID = :offsetTxId ::text::xid8, OFFSET_ID = :offsetId
    WHERE SUBSCRIPTION_NAME = :subscriptionName
    """, nativeQuery = true)
  int updateEventSubscription(
    @Param("subscriptionName") @Nonnull String subscriptionName,
    @Param("offsetTxId") @Nonnull Long offsetTxId,
    @Param("offsetId") @Nonnull Long offsetId
  );
}
