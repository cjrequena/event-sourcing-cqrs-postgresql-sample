package com.cjrequena.sample.component;

import com.cjrequena.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.domain.command.Command;
import com.cjrequena.sample.domain.command.CommandHandler;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CreateAccountCommandHandler implements CommandHandler<CreateAccountCommand> {

  @Override
  public void handle(Command command, Aggregate aggregate) {
    log.trace("Handling {} ", CreateAccountCommand.class );
    log.trace("Command {}", command);
    log.trace("Aggregate {}", aggregate);
  }

  @Nonnull
  @Override
  public Class<CreateAccountCommand> getCommandType() {
    return CreateAccountCommand.class;
  }
}
