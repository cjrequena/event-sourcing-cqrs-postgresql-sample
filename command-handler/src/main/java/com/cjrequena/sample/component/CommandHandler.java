package com.cjrequena.sample.component;

import com.cjrequena.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.domain.command.Command;
import jakarta.annotation.Nonnull;

public interface CommandHandler< T extends Command> {

    void handle(Command command, Aggregate aggregate);

    @Nonnull
    Class<T> getCommandType();
}
