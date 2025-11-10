package com.cjrequena.sample.domain.model.command;

import com.cjrequena.eventstore.sample.domain.model.command.Command;
import com.cjrequena.sample.domain.model.aggregate.AggregateType;
import com.cjrequena.sample.domain.model.vo.CreditVO;
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
    super(aggregateId, AggregateType.ACCOUNT_AGGREGATE.getType());
    this.creditVO = creditVO;
  }

}
