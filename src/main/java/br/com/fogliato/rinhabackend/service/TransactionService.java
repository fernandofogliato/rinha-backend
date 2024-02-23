package br.com.fogliato.rinhabackend.service;

import br.com.fogliato.rinhabackend.entity.CustomerEntity;
import br.com.fogliato.rinhabackend.entity.TransactionEntity;
import br.com.fogliato.rinhabackend.exception.InvalidTransactionException;
import br.com.fogliato.rinhabackend.exception.ResourceNotFoundException;
import br.com.fogliato.rinhabackend.model.Extrato;
import br.com.fogliato.rinhabackend.model.Saldo;
import br.com.fogliato.rinhabackend.model.Transacao;
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

    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Saldo create(long customerId, Transacao transaction) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);
        long value = transaction.tipo().equals(TransactionType.d) ? transaction.valor() * -1 : transaction.valor();
        long newBalance = customer.getBalance() + value;

        if (Math.abs(newBalance) > customer.getLimit()) {
            throw new InvalidTransactionException();
        }
        transactionRepository.save(transaction.toEntity(customerId));
        customerRepository.updateBalance(customerId, value);
        return new Saldo(newBalance, Instant.now(), customer.getLimit());
    }

    public Extrato getBankStatement(long customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);
        List<TransactionEntity> entities = transactionRepository.findLastTransactions(customerId);
        List<Transacao> transactions = entities.stream().map(Transacao::fromEntity).toList();
        Saldo saldo = Saldo.fromEntity(customer);
        return new Extrato(saldo, transactions);
    }
}
