package br.fogliato.rinhabackend.service;

import br.fogliato.rinhabackend.entity.CustomerEntity;
import br.fogliato.rinhabackend.entity.TransactionEntity;
import br.fogliato.rinhabackend.exception.InvalidTransactionException;
import br.fogliato.rinhabackend.exception.ResourceNotFoundException;
import br.fogliato.rinhabackend.model.Extrato;
import br.fogliato.rinhabackend.model.Saldo;
import br.fogliato.rinhabackend.model.Transacao;
import br.fogliato.rinhabackend.repository.CustomerRepository;
import br.fogliato.rinhabackend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        long newBalance = customer.getBalance() + transaction.valor();

        if (Math.abs(newBalance) > customer.getLimit()) {
            throw new InvalidTransactionException();
        }
        transactionRepository.save(transaction.toEntity(customerId));
        customerRepository.updateBalance(customerId, transaction.valor());
        return Saldo.fromEntity(customer);
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
