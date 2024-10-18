package com.cjrequena.sample.domain.command;

import jakarta.annotation.Nonnull;

public interface CommandHandler< T extends AbstractCommand> {

    //void handle(Aggregate aggregate, Command command);
    void handle();

    @Nonnull
    Class<T> getCommandType();
}
