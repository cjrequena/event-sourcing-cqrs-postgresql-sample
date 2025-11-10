package com.cjrequena.sample.domain.model.command;

import com.cjrequena.eventstore.sample.domain.model.command.Command;
import com.cjrequena.sample.domain.model.aggregate.AggregateType;
import com.cjrequena.sample.domain.model.vo.DebitVO;
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

}
