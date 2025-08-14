package com.cjrequena.sample.domain.command;

import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.domain.vo.DebitVO;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class DebitAccountCommand extends Command {

  private final DebitVO debitVO;

  @Builder
  public DebitAccountCommand(@Nonnull UUID aggregateId, DebitVO debitVO) {
    super(aggregateId, AggregateType.ACCOUNT_AGGREGATE.getType());
    this.debitVO = debitVO;
  }

  private static UUID generateAggregateId() {
    return UUID.randomUUID();
  }
}
