package br.com.fogliato.rinhabackend.service;

import br.com.fogliato.rinhabackend.entity.BalanceEntity;
import br.com.fogliato.rinhabackend.entity.CustomerEntity;
import br.com.fogliato.rinhabackend.entity.TransactionEntity;
import br.com.fogliato.rinhabackend.exception.InvalidTransactionException;
import br.com.fogliato.rinhabackend.exception.ResourceNotFoundException;
import br.com.fogliato.rinhabackend.model.Extrato;
import br.com.fogliato.rinhabackend.model.Saldo;
import br.com.fogliato.rinhabackend.model.Transacao;
import br.com.fogliato.rinhabackend.repository.BalanceRepository;
import br.com.fogliato.rinhabackend.repository.CustomerRepository;
import br.com.fogliato.rinhabackend.repository.TransactionRepository;
import br.com.fogliato.rinhabackend.type.TransactionType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final BalanceRepository balanceRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CustomerRepository customerRepository,
                              BalanceRepository balanceRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.balanceRepository = balanceRepository;
    }

    @Transactional
    public Saldo create(int customerId, Transacao transaction) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        int value = transaction.tipo().equals(TransactionType.d) ? transaction.valor() * -1 : transaction.valor();
        BalanceEntity lastBalance = balanceRepository.findLastBalance(customerId);
        int newBalance = lastBalance.getBalance() + value;

        if (Math.abs(newBalance) > customer.getLimit()) {
            throw new InvalidTransactionException();
        }
        transactionRepository.save(transaction.toEntity(customerId));
        balanceRepository.save(new BalanceEntity(customerId, newBalance));
        return new Saldo(newBalance, Instant.now(), customer.getLimit());
    }

    public Extrato getBankStatement(int customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);
        List<TransactionEntity> entities = transactionRepository.findLastTransactions(customerId);
        List<Transacao> transactions = entities.stream().map(Transacao::fromEntity).toList();
        BalanceEntity lastBalance = balanceRepository.findLastBalance(customerId);
        Saldo saldo = Saldo.fromEntity(customer, lastBalance);
        return new Extrato(saldo, transactions);
    }
}
