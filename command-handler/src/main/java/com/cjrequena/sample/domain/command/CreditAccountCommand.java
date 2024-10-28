package com.cjrequena.sample.domain.command;

import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.vo.CreditVO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class CreditAccountCommand extends Command {

  private final CreditVO creditVO;

  @Builder
  public CreditAccountCommand(UUID aggregateId, @NotNull CreditVO creditVO) {
    super(aggregateId, AggregateType.ACCOUNT_AGGREGATE.getAggregateType());
    this.creditVO = creditVO;
  }

  private static UUID generateAggregateId() {
    return UUID.randomUUID();
  }
}
