package com.cjrequena.sample.api;

import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.command.CreateAccountCommand;
import com.cjrequena.sample.domain.command.CreditAccountCommand;
import com.cjrequena.sample.domain.command.DebitAccountCommand;
import com.cjrequena.sample.dto.AccountDTO;
import com.cjrequena.sample.dto.CreditDTO;
import com.cjrequena.sample.dto.DebitDTO;
import com.cjrequena.sample.exception.api.BadRequestApiException;
import com.cjrequena.sample.exception.api.ConflictApiException;
import com.cjrequena.sample.exception.api.NotFoundApiException;
import com.cjrequena.sample.exception.api.NotImplementedApiException;
import com.cjrequena.sample.exception.service.AccountBalanceServiceException;
import com.cjrequena.sample.exception.service.AggregateNotFoundServiceException;
import com.cjrequena.sample.exception.service.AmountServiceException;
import com.cjrequena.sample.exception.service.CommandHandlerNotFoundServiceException;
import com.cjrequena.sample.mapper.AccountMapper;
import com.cjrequena.sample.mapper.EventMapper;
import com.cjrequena.sample.service.CommandHandlerService;
import com.cjrequena.sample.vo.CreditVO;
import com.cjrequena.sample.vo.DebitVO;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

  private final CommandHandlerService commandHandlerService;

  private final AccountMapper accountMapper;
  private final EventStoreService eventStoreService;
  private final AggregateFactory aggregateFactory;
  private final EventMapper eventMapper;

  @SneakyThrows
  @PostMapping(
    path = "/accounts",
    produces = {APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<String> create(@Parameter @Valid @RequestBody AccountDTO accountDTO, ServerHttpRequest request) {
    try {
      Command command = CreateAccountCommand.builder()
        .accountVO(accountMapper.toAccountVO(accountDTO))
        .build();
      this.commandHandlerService.handler(command);
      return new ResponseEntity<>("Create successful", HttpStatus.OK);
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new ConflictApiException(ex.getMessage());
    } catch (CommandHandlerNotFoundServiceException ex) {
      throw new NotImplementedApiException(ex.getMessage());
    } catch (AccountBalanceServiceException ex) {
      throw new BadRequestApiException(ex.getMessage());
    }
  }

  @SneakyThrows
  @PutMapping("/accounts/{accountId}/credit")
  public ResponseEntity<String> creditAccount(@PathVariable("accountId") UUID accountId, @RequestBody CreditDTO creditDTO) {
    // Logic to handle crediting the account
    try {
      Command command = CreditAccountCommand.builder()
        .aggregateId(accountId)
        .creditVO(CreditVO.builder()
          .accountId(accountId)
          .amount(creditDTO.getAmount())
          .build())
        .build();
      this.commandHandlerService.handler(command);
      return new ResponseEntity<>("Credit successful", HttpStatus.OK);
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new ConflictApiException(ex.getMessage());
    } catch (CommandHandlerNotFoundServiceException ex) {
      throw new NotImplementedApiException(ex.getMessage());
    } catch (AmountServiceException ex) {
      throw new BadRequestApiException(ex.getMessage());
    }
  }

  @SneakyThrows
  @PutMapping("/accounts/{accountId}/debit")
  public ResponseEntity<String> debitAccount(@PathVariable("accountId") UUID accountId, @RequestBody DebitDTO debitDTO) {
    // Logic to handle debiting the account
    try {
      Command command = DebitAccountCommand.builder()
        .aggregateId(accountId)
        .debitVO(DebitVO.builder()
          .accountId(accountId)
          .amount(debitDTO.getAmount())
          .build())
        .build();
      this.commandHandlerService.handler(command);
      return new ResponseEntity<>("Debit successful", HttpStatus.OK);
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new ConflictApiException(ex.getMessage());
    } catch (CommandHandlerNotFoundServiceException ex) {
      throw new NotImplementedApiException(ex.getMessage());
    } catch (AmountServiceException | AccountBalanceServiceException ex) {
      throw new BadRequestApiException(ex.getMessage());
    } catch (AggregateNotFoundServiceException ex) {
      throw new NotFoundApiException(ex.getMessage());
    }
  }
}
