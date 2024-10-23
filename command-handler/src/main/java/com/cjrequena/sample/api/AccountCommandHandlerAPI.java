package com.cjrequena.sample.api;

import com.cjrequena.sample.domain.aggregate.AccountAggregate;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.dto.AccountDTO;
import com.cjrequena.sample.dto.CreditDTO;
import com.cjrequena.sample.dto.DebitDTO;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.mapper.AccountMapper;
import com.cjrequena.sample.service.EventStoreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/command-handler")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountCommandHandlerAPI {

  private final AccountMapper accountMapper;
  private final EventStoreService eventStoreService;

  @PostMapping(
    path = "/accounts",
    produces = {APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<String> create(@Parameter @Valid @RequestBody AccountDTO accountDTO, ServerHttpRequest request)
    throws JsonProcessingException, OptimisticConcurrencyServiceException {

    CreateAccountCommand command = CreateAccountCommand.builder()
      .accountVO(accountMapper.toAccountVO(accountDTO))
      .build();

    AccountAggregate aggregate = AccountAggregate.builder()
      .aggregateId(command.getAggregateId())
      .version(0L)
      .build();

    aggregate.applyCommand(command);

    eventStoreService.saveAggregate(aggregate);

    return new ResponseEntity<>("Create successful", HttpStatus.OK);
  }

  @PostMapping("/accounts/{accountId}/credit")
  public ResponseEntity<String> creditAccount(@PathVariable("accountId") UUID accountId, @RequestBody CreditDTO creditDTO)
    throws JsonProcessingException, OptimisticConcurrencyServiceException {
    // Logic to handle crediting the account
    return new ResponseEntity<>("Credit successful", HttpStatus.OK);
  }

  @PostMapping("/{accountId}/debit")
  public ResponseEntity<String> debitAccount(@PathVariable("accountId") UUID accountId, @RequestBody DebitDTO debitDTO)
    throws JsonProcessingException, OptimisticConcurrencyServiceException {
    // Logic to handle debiting the account
    return new ResponseEntity<>("Debit successful", HttpStatus.OK);
  }

}
