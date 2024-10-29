package com.cjrequena.sample.mapper;

import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.sample.common.util.JsonUtil;
import com.cjrequena.sample.domain.event.AccountCreatedEvent;
import com.cjrequena.sample.domain.event.AccountCreditedEvent;
import com.cjrequena.sample.domain.event.AccountDebitedEvent;
import com.cjrequena.sample.exception.service.EventMapperServiceException;
import com.cjrequena.sample.vo.AccountVO;
import com.cjrequena.sample.vo.CreditVO;
import com.cjrequena.sample.vo.DebitVO;
import com.cjrequena.sample.vo.EventExtensionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
  componentModel = "spring",
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface EventMapper {

  Logger log = LoggerFactory.getLogger(EventMapper.class);

  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  //@Mapping(source = "aggregateId", target = "aggregateId")
  EventEntity mapToEventEntity(AccountCreatedEvent event);
  EventEntity mapToEventEntity(AccountCreditedEvent event);
  EventEntity mapToEventEntity(AccountDebitedEvent event);
  AccountCreatedEvent mapToAccountCreatedEvent(EventEntity eventEntity);
  AccountCreditedEvent mapToAccountCreditedEvent(EventEntity eventEntity);
  AccountDebitedEvent mapToAccountDebitedEvent(EventEntity eventEntity);

  // Custom mapping method for converting String to EventExtensionVO
  default EventExtensionVO mapToEventExtensionVO(String value) {
    try {
      return JsonUtil.jsonStringToObject(value, EventExtensionVO.class);
    } catch (JsonProcessingException ex) {
      String errorMessage = String.format("Failed to map JSON string to EventExtensionVO: %s", value);
      log.error(errorMessage);
      throw new EventMapperServiceException(errorMessage, ex);
    }
  }

  // You can add another mapping method if you need to go from EventExtensionVO to String
  default String map(EventExtensionVO value) {
    try {
      return JsonUtil.objectToJsonString(value);
    } catch (JsonProcessingException ex) {
      String errorMessage = String.format("Failed to map EventExtensionVO to JSON string: %s", value);
      log.error(errorMessage);
      throw new EventMapperServiceException(errorMessage, ex);
    }
  }

  // Custom mapping method for converting String to AccountVO
  default AccountVO mapToAccountVO(String value) {
    try {
      return JsonUtil.jsonStringToObject(value, AccountVO.class);
    } catch (JsonProcessingException ex) {
      String errorMessage = String.format("Failed to map JSON string to AccountVO: %s", value);
      log.error(errorMessage);
      throw new EventMapperServiceException(errorMessage, ex);
    }
  }

  // You can add another mapping method if you need to go from AccountVO to String
  default String map(AccountVO value) {
    try {
      return JsonUtil.objectToJsonString(value);
    } catch (JsonProcessingException ex) {
      String errorMessage = String.format("Failed to map AccountVO to JSON string: %s", value);
      log.error(errorMessage);
      throw new EventMapperServiceException(errorMessage, ex);
    }
  }

  // Custom mapping method for converting String to CreditVO
  default CreditVO mapToCreditVO(String value) {
    try {
      return JsonUtil.jsonStringToObject(value, CreditVO.class);
    } catch (JsonProcessingException ex) {
      String errorMessage = String.format("Failed to map JSON string to CreditVO: %s", value);
      log.error(errorMessage);
      throw new EventMapperServiceException(errorMessage, ex);
    }
  }

  // You can add another mapping method if you need to go from CreditVO to String
  default String map(CreditVO value) {
    try {
      return JsonUtil.objectToJsonString(value);
    } catch (JsonProcessingException ex) {
      String errorMessage = String.format("Failed to map CreditVO to JSON string: %s", value);
      log.error(errorMessage);
      throw new EventMapperServiceException(errorMessage, ex);
    }
  }

  // Custom mapping method for converting String to DebitVO
  default DebitVO mapToDebitVO(String value) {
    try {
      return JsonUtil.jsonStringToObject(value, DebitVO.class);
    } catch (JsonProcessingException ex) {
      String errorMessage = String.format("Failed to map JSON string to DebitVO: %s", value);
      log.error(errorMessage);
      throw new EventMapperServiceException(errorMessage, ex);
    }
  }

  // You can add another mapping method if you need to go from DebitVO to String
  default String map(DebitVO value) {
    try {
      return JsonUtil.objectToJsonString(value);
    } catch (JsonProcessingException ex) {
      String errorMessage = String.format("Failed to map DebitVO to JSON string: %s", value);
      log.error(errorMessage);
      throw new EventMapperServiceException(errorMessage, ex);
    }
  }

  // New method to map a List of EventEntity to a List of Event
  default List<Event> mapToEventList(List<EventEntity> eventEntities) {
    return eventEntities.stream()
      .map(this::mapToEvent)  // Call the helper method for individual mapping
      .collect(Collectors.toList());
  }

  // Helper method to map a single EventEntity to an Event (AccountCreatedEvent, AccountCreditedEvent, etc.)
  default Event mapToEvent(EventEntity eventEntity) {
    // Assuming EventEntity has a type field or some kind of discriminator
    switch (eventEntity.getEventType()) {
      case "com.cjrequena.sample.domain.event.AccountCreatedEvent":
        return mapToAccountCreatedEvent(eventEntity);
      case "com.cjrequena.sample.domain.event.AccountCreditedEvent":
        return mapToAccountCreditedEvent(eventEntity);
      case "com.cjrequena.sample.domain.event.AccountDebitedEvent":
        return mapToAccountDebitedEvent(eventEntity);
      default:
        String errorMessage = String.format("Error mapping to event, unknown event type: %s", eventEntity.getEventType());
        log.error(errorMessage);
        throw new EventMapperServiceException(errorMessage);
    }
  }

}
