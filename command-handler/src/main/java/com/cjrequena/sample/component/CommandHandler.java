package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import jakarta.annotation.Nonnull;

public interface CommandHandler< T extends Command> {

    void handle(Command command, Aggregate aggregate);

    @Nonnull
    Class<T> getCommandType();
}
