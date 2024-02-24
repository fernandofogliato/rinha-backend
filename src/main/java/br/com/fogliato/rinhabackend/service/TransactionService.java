package br.com.fogliato.rinhabackend.service;

import br.com.fogliato.rinhabackend.entity.CustomerEntity;
import br.com.fogliato.rinhabackend.entity.TransactionEntity;
import br.com.fogliato.rinhabackend.exception.InvalidTransactionException;
import br.com.fogliato.rinhabackend.exception.ResourceNotFoundException;
import br.com.fogliato.rinhabackend.model.Balance;
import br.com.fogliato.rinhabackend.model.BankStatement;
import br.com.fogliato.rinhabackend.model.Transaction;
import br.com.fogliato.rinhabackend.repository.BalanceRepository;
import br.com.fogliato.rinhabackend.repository.CustomerRepository;
import br.com.fogliato.rinhabackend.repository.TransactionRepository;
import br.com.fogliato.rinhabackend.type.TransactionType;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              BalanceRepository balanceRepository,
                              CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Retryable(retryFor = { CannotAcquireLockException.class }, maxAttempts = 3)
    public Balance create(int customerId, Transaction transaction) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        int newBalance = getNewBalance(transaction, customer.getBalance());

        // A debit transaction cannot debit more than the limit of the customer
        if (transaction.type().equals(TransactionType.d) && Math.abs(newBalance) > customer.getLimit()) {
            throw new InvalidTransactionException();
        }
        transactionRepository.save(transaction.toEntity(customerId));
        customerRepository.updateBalance(customerId, newBalance);
//        balanceRepository.save(new BalanceEntity(customerId, newBalance));
        return Balance.withBalanceAndLimit(newBalance, customer.getLimit());
    }

    private static int getNewBalance(Transaction transaction, int currentBalance) {
        int value = transaction.type().equals(TransactionType.d) ? transaction.value() * -1 : transaction.value();
        return currentBalance + value;
    }

    public BankStatement getBankStatement(int customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);
        List<TransactionEntity> entities = transactionRepository.findLastTransactions(customerId);
        List<Transaction> transactions = entities.stream().map(Transaction::fromEntity).toList();

        Balance balance = Balance.withBalanceAndLimit(customer.getBalance(), customer.getLimit());
        return new BankStatement(balance, transactions);
    }
}
