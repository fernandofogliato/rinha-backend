package br.com.fogliato.rinhabackend.controller;

import br.com.fogliato.rinhabackend.dto.TransacaoCreatedResponse;
import br.com.fogliato.rinhabackend.model.BankStatement;
import br.com.fogliato.rinhabackend.model.Balance;
import br.com.fogliato.rinhabackend.model.Transaction;
import br.com.fogliato.rinhabackend.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class CustomerController {

    private final TransactionService transactionService;

    public CustomerController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{customerId}/transacoes")
    public TransacaoCreatedResponse createTransaction(@PathVariable int customerId, @RequestBody @Valid Transaction transaction) {
        Balance balance = transactionService.create(customerId, transaction);
        return new TransacaoCreatedResponse(balance.limit(), balance.total());
    }

    @GetMapping("/{customerId}/extrato")
    public BankStatement getBankStatement(@PathVariable int customerId) {
        return transactionService.getBankStatement(customerId);
    }

}
