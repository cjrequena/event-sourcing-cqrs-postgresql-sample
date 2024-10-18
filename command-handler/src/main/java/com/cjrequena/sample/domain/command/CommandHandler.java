package com.cjrequena.sample.domain.command;

import jakarta.annotation.Nonnull;

public interface CommandHandler< T extends Command> {

    //void handle(Aggregate aggregate, Command command);
    void handle();

    @Nonnull
    Class<T> getCommandType();
}
