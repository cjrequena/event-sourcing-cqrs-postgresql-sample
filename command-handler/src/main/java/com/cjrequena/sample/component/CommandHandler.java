package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import jakarta.annotation.Nonnull;

public interface CommandHandler< T extends Command> {

    void handle(@Nonnull Command command, @Nonnull Aggregate aggregate) throws EventStoreOptimisticConcurrencyServiceException;

    @Nonnull
    Class<T> getCommandType();
}
