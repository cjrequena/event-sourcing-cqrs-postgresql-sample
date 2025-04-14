package com.cjrequena.sample.domain.command;

import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.vo.AccountVO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class CreateAccountCommand extends Command {

  private final AccountVO accountVO;

  @Builder
  public CreateAccountCommand(UUID aggregateId, @NotNull AccountVO accountVO) {
    super(generateAggregateId(), AggregateType.ACCOUNT_AGGREGATE.getType());
    this.accountVO = AccountVO
      .builder()
      .id(getAggregateId())
      .owner(accountVO.getOwner())
      .balance(accountVO.getBalance())
      .build();
  }

  private static UUID generateAggregateId() {
    return UUID.randomUUID();
  }
}
