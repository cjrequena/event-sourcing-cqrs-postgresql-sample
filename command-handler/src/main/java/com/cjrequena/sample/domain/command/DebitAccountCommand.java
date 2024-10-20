package com.cjrequena.sample.domain.command;

import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.vo.DebitVO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class DebitAccountCommand extends Command {

  private final DebitVO debitVO;

  @Builder
  public DebitAccountCommand(@NotNull DebitVO debitVO) {
    super(generateAggregateId(), AggregateType.ACCOUNT_AGGREGATE.getValue());
    this.debitVO = debitVO;
  }

  private static UUID generateAggregateId() {
    return UUID.randomUUID();
  }
}
