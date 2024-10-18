package com.cjrequena.sample.domain.command;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateAccountCommandHandler implements CommandHandler<CreateAccountCommand> {

  @Override
  public void handle() {
    log.info("Handling {} ", CreateAccountCommand.class );
  }

  @Nonnull
  @Override
  public Class<CreateAccountCommand> getCommandType() {
    return CreateAccountCommand.class;
  }
}
