package com.cjrequena.sample.domain.command;

import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.vo.AccountVO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class CreateAccountCommand extends AbstractCommand{

  private final AccountVO accountVO;

  public CreateAccountCommand(@NotNull AccountVO accountVO) {
    super(generateAggregateId(), AggregateType.ACCOUNT_AGGREGATE.getValue());
    this.accountVO = accountVO;
  }

  private static UUID generateAggregateId() {
    return UUID.randomUUID();
  }
}
