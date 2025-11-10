package com.cjrequena.eventstore.sample.domain.model.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
public abstract class Command {

  @NotNull
  protected final UUID aggregateId;

  @NotNull
  protected final String aggregateType;

}
