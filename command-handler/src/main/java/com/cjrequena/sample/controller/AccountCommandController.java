package com.cjrequena.sample.controller;

import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.controller.dto.AccountDTO;
import com.cjrequena.sample.controller.dto.CreditDTO;
import com.cjrequena.sample.controller.dto.DebitDTO;
import com.cjrequena.sample.controller.exception.BadRequestException;
import com.cjrequena.sample.controller.exception.ConflictException;
import com.cjrequena.sample.controller.exception.NotFoundException;
import com.cjrequena.sample.controller.exception.NotImplementedException;
import com.cjrequena.sample.domain.exception.AccountBalanceException;
import com.cjrequena.sample.domain.exception.AggregateNotFoundException;
import com.cjrequena.sample.domain.exception.AmountException;
import com.cjrequena.sample.domain.exception.CommandHandlerNotFoundException;
import com.cjrequena.sample.domain.mapper.AccountMapper;
import com.cjrequena.sample.domain.mapper.EventMapper;
import com.cjrequena.sample.domain.model.command.CreateAccountCommand;
import com.cjrequena.sample.domain.model.command.CreditAccountCommand;
import com.cjrequena.sample.domain.model.command.DebitAccountCommand;
import com.cjrequena.sample.domain.model.vo.CreditVO;
import com.cjrequena.sample.domain.model.vo.DebitVO;
import com.cjrequena.sample.service.command.CommandBusService;
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

import static com.cjrequena.sample.shared.common.Constant.VND_SAMPLE_SERVICE_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = AccountCommandController.ENDPOINT, headers = {AccountCommandController.ACCEPT_VERSION})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountCommandController {

  public static final String ENDPOINT = "/api/command-handler";
  public static final String ACCEPT_VERSION = "Accept-Version=" + VND_SAMPLE_SERVICE_V1;

  private final CommandBusService commandBusService;
  private final EventStoreService eventStoreService;
  private final EventMapper eventMapper;
  private final AccountMapper accountMapper;

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
      this.commandBusService.handle(command);
      return new ResponseEntity<>("Create successful", HttpStatus.OK);
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new ConflictException(ex.getMessage());
    } catch (CommandHandlerNotFoundException ex) {
      throw new NotImplementedException(ex.getMessage());
    } catch (AccountBalanceException ex) {
      throw new BadRequestException(ex.getMessage());
    }
  }

  @SneakyThrows
  @PutMapping("/accounts/credit")
  public ResponseEntity<String> creditAccount( @RequestBody CreditDTO creditDTO) {
    // Logic to handle crediting the account
    try {
      final UUID accountId = creditDTO.getAccountId();
      Command command = CreditAccountCommand.builder()
        .aggregateId(accountId)
        .creditVO(CreditVO.builder()
          .accountId(accountId)
          .amount(creditDTO.getAmount())
          .build())
        .build();
      this.commandBusService.handle(command);
      return new ResponseEntity<>("Credit successful", HttpStatus.OK);
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new ConflictException(ex.getMessage());
    } catch (CommandHandlerNotFoundException ex) {
      throw new NotImplementedException(ex.getMessage());
    } catch (AmountException ex) {
      throw new BadRequestException(ex.getMessage());
    } catch (AggregateNotFoundException ex){
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }

  @SneakyThrows
  @PutMapping("/accounts/debit")
  public ResponseEntity<String> debitAccount(@RequestBody DebitDTO debitDTO) {
    // Logic to handle debiting the account
    try {
      final UUID accountId = debitDTO.getAccountId();
      Command command = DebitAccountCommand.builder()
        .aggregateId(accountId)
        .debitVO(DebitVO.builder()
          .accountId(accountId)
          .amount(debitDTO.getAmount())
          .build())
        .build();
      this.commandBusService.handle(command);
      return new ResponseEntity<>("Debit successful", HttpStatus.OK);
    } catch (EventStoreOptimisticConcurrencyServiceException ex) {
      throw new ConflictException(ex.getMessage());
    } catch (CommandHandlerNotFoundException ex) {
      throw new NotImplementedException(ex.getMessage());
    } catch (AmountException | AccountBalanceException ex) {
      throw new BadRequestException(ex.getMessage());
    } catch (AggregateNotFoundException ex) {
      throw new NotFoundException(ex.getMessage());
    }
  }
}
