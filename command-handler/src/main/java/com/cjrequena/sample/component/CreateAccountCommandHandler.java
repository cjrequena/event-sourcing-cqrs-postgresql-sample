package com.cjrequena.sample.component;

import com.cjrequena.sample.domain.command.CommandHandler;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
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
