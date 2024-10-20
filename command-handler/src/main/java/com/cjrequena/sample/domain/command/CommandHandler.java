package com.cjrequena.sample.domain.command;

import com.cjrequena.sample.domain.aggregate.Aggregate;
import jakarta.annotation.Nonnull;

public interface CommandHandler< T extends Command> {

    void handle(Command command, Aggregate aggregate);

    @Nonnull
    Class<T> getCommandType();
}
