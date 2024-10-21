package com.cjrequena.sample.mapper;

import com.cjrequena.sample.domain.event.AccountCreatedEvent;
import com.cjrequena.sample.domain.event.AccountCreditedEvent;
import com.cjrequena.sample.domain.event.AccountDebitedEvent;
import com.cjrequena.sample.entity.AccountCreatedEventEntity;
import com.cjrequena.sample.entity.AccountCreditedEventEntity;
import com.cjrequena.sample.entity.AccountDebitedEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    // Map from Event to EventEntity
    @Mapping(source = "aggregateId", target = "aggregateId")
    @Mapping(source = "aggregateVersion", target = "aggregateVersion")
    @Mapping(source = "eventType", target = "eventType")
    @Mapping(source = "dataContentType", target = "dataContentType")
    @Mapping(source = "data", target = "data")
    @Mapping(source = "dataBase64", target = "dataBase64")
    @Mapping(source = "extension", target = "extension")
    @Mapping(source = "time", target = "time")
    AccountCreatedEventEntity toEventEntity(AccountCreatedEvent event);


    // Map from Event to EventEntity
    @Mapping(source = "aggregateId", target = "aggregateId")
    @Mapping(source = "aggregateVersion", target = "aggregateVersion")
    @Mapping(source = "eventType", target = "eventType")
    @Mapping(source = "dataContentType", target = "dataContentType")
    @Mapping(source = "data", target = "data")
    @Mapping(source = "dataBase64", target = "dataBase64")
    @Mapping(source = "extension", target = "extension")
    @Mapping(source = "time", target = "time")
    AccountCreditedEventEntity toEventEntity(AccountCreditedEvent event);

    // Map from Event to EventEntity
    @Mapping(source = "aggregateId", target = "aggregateId")
    @Mapping(source = "aggregateVersion", target = "aggregateVersion")
    @Mapping(source = "eventType", target = "eventType")
    @Mapping(source = "dataContentType", target = "dataContentType")
    @Mapping(source = "data", target = "data")
    @Mapping(source = "dataBase64", target = "dataBase64")
    @Mapping(source = "extension", target = "extension")
    @Mapping(source = "time", target = "time")
    AccountDebitedEventEntity toEventEntity(AccountDebitedEvent event);



    // Map from EventEntity to Event
    @Mapping(source = "aggregateId", target = "aggregateId")
//    @Mapping(source = "aggregateVersion", target = "aggregateVersion")
//    @Mapping(source = "eventType", target = "eventType")
//    @Mapping(source = "dataContentType", target = "dataContentType")
//    @Mapping(source = "data", target = "accountVO")
//    @Mapping(source = "dataBase64", target = "dataBase64")
//    @Mapping(source = "time", target = "time")
    AccountCreatedEvent toEvent(AccountCreditedEventEntity eventEntity);

    // Map from EventEntity to Event
    @Mapping(source = "aggregateId", target = "aggregateId")
//    @Mapping(source = "aggregateVersion", target = "aggregateVersion")
//    @Mapping(source = "eventType", target = "eventType")
//    @Mapping(source = "dataContentType", target = "dataContentType")
//    @Mapping(source = "data", target = "credit")
//    @Mapping(source = "dataBase64", target = "dataBase64")
//    @Mapping(source = "time", target = "time")
    AccountCreditedEvent toEvent(AccountCreatedEventEntity eventEntity);

    // Map from EventEntity to Event
    @Mapping(source = "aggregateId", target = "aggregateId")
//    @Mapping(source = "aggregateVersion", target = "aggregateVersion")
//    @Mapping(source = "eventType", target = "eventType")
//    @Mapping(source = "dataContentType", target = "dataContentType")
//    @Mapping(source = "data", target = "debit")
//    @Mapping(source = "dataBase64", target = "dataBase64")
//    @Mapping(source = "time", target = "time")
    AccountDebitedEvent toEvent(AccountDebitedEventEntity eventEntity);
}
