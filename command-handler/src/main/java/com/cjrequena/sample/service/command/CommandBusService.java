package com.cjrequena.sample.service.command;

import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.sample.exception.service.CommandHandlerNotFoundServiceException;
import com.cjrequena.sample.service.projection.ProjectionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandBusService {

  private final List<CommandHandler<? extends Command>> commandHandlers;
  private final List<ProjectionHandler> projectionHandlers;

  public void handle(Command command) {
    log.info("Processing command {}", command);

    commandHandlers.stream()
      .filter(commandHandler -> commandHandler.getCommandType() == command.getClass())
      .findFirst()
      .ifPresentOrElse(commandHandler -> {
        log.info("Handling command {} with {}", command.getClass().getSimpleName(), commandHandler.getClass().getSimpleName());

        final Aggregate aggregate = commandHandler.handle(command);

        // Save or Update the projection database
        projectionHandlers.parallelStream()
          .filter(handler -> handler.getAggregateType().getType().equals(aggregate.getAggregateType()))
          .forEach(handler -> handler.handle(aggregate));

      }, () -> {
        log.info("No specialized handler found with {}", command.getClass().getSimpleName());
        throw new CommandHandlerNotFoundServiceException("No specialized handler found for command: " + command.getClass().getSimpleName());
      });
  }
}
