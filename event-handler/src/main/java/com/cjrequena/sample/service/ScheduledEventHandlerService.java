package com.cjrequena.sample.service;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.component.AsyncEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@ConditionalOnProperty(name = "eventstore.subscription.enabled", havingValue = "true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ScheduledEventHandlerService {

  private final EventStoreService eventStoreService;
  private final List<AsyncEventHandler> eventHandlers;
  private final EventStoreConfigurationProperties eventStoreConfigurationProperties;

  @Scheduled(
    fixedDelayString = "${eventstore.subscription.polling-interval}",
    initialDelayString = "${eventstore.subscription.polling-initial-delay}"
  )
  public void handler() {
    eventHandlers.forEach(this::handler);
  }

  @Async
  public void handler(AsyncEventHandler eventHandler) {
    final EventStoreConfigurationProperties.SubscriptionProperties subscriptionProperties;
    subscriptionProperties = this.eventStoreConfigurationProperties.getSubscription();

    String subscriptionName = subscriptionProperties.name();
    log.debug("Handling new events for subscription {}", subscriptionName);

    this.eventStoreService.registerNewSubscriptionIfAbsent(subscriptionName);

    this.eventStoreService.retrieveEventSubscriptionAndLockSubscriptionOffset(subscriptionName).ifPresentOrElse(
      eventSubscription -> {
        log.debug("Acquired lock on subscription {}, eventSubscription = {}", subscriptionName, eventSubscription);

        List<EventEntity> events = eventStoreService.retrieveEventsByAggregateTypeAfterOffsetTxIAndOffsetId(
          eventHandler.getAggregateType(),
          eventSubscription.getOffsetTxId(),
          eventSubscription.getOffsetId()
        );

        if (!events.isEmpty()) {
          log.debug("Fetched {} new event(s) for subscription {}", events.size(), subscriptionName);
          eventHandler.handle(events);
          EventEntity lastEvent = events.getLast();
          this.eventStoreService.updateEventSubscription(subscriptionName, lastEvent.getOffsetTxId(), lastEvent.getOffsetId());
        }
      },
      () -> log.info("Can't acquire lock on subscription {}", subscriptionName)
    );
  }
}
