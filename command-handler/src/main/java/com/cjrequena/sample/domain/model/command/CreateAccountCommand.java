package com.cjrequena.sample.domain.model.command;

import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.sample.domain.model.aggregate.AggregateType;
import com.cjrequena.sample.domain.model.vo.AccountVO;
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
  public CreateAccountCommand(@NotNull AccountVO accountVO) {
    super(UUID.randomUUID(), AggregateType.ACCOUNT_AGGREGATE.getType());
    this.accountVO = AccountVO
      .builder()
      .id(getAggregateId())
      .owner(accountVO.owner())
      .balance(accountVO.balance())
      .build();
  }
}
