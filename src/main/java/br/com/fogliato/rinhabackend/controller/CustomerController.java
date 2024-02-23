package br.com.fogliato.rinhabackend.controller;

import br.com.fogliato.rinhabackend.dto.TransacaoCreatedResponse;
import br.com.fogliato.rinhabackend.model.Extrato;
import br.com.fogliato.rinhabackend.model.Saldo;
import br.com.fogliato.rinhabackend.model.Transacao;
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
    public TransacaoCreatedResponse createTransaction(@PathVariable long customerId, @RequestBody @Valid Transacao transaction) {
        Saldo saldo = transactionService.create(customerId, transaction);
        return new TransacaoCreatedResponse(saldo.limite(), saldo.total());
    }

    @GetMapping("/{customerId}/extrato")
    public Extrato getBankStatement(@PathVariable long customerId) {
        return transactionService.getBankStatement(customerId);
    }

}
