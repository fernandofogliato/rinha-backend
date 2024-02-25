package br.com.fogliato.rinhabackend.service;

import br.com.fogliato.rinhabackend.entity.CustomerEntity;
import br.com.fogliato.rinhabackend.entity.TransactionEntity;
import br.com.fogliato.rinhabackend.exception.InvalidTransactionException;
import br.com.fogliato.rinhabackend.exception.ResourceNotFoundException;
import br.com.fogliato.rinhabackend.model.Balance;
import br.com.fogliato.rinhabackend.model.BankStatement;
import br.com.fogliato.rinhabackend.model.Transaction;
import br.com.fogliato.rinhabackend.model.UpdatedBalance;
import br.com.fogliato.rinhabackend.repository.CustomerRepository;
import br.com.fogliato.rinhabackend.repository.TransactionRepository;
import br.com.fogliato.rinhabackend.type.TransactionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private static final int ERROR_INVALID_TRANSACTION = 2;
    private static final int ERROR_CUSTOMER_NOT_EXIST = 1;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public Balance create(int customerId, Transaction transaction) {
        UpdatedBalance response = transactionRepository.addTransaction(
                customerId,
                transaction.value().intValue(),
                transaction.description(),
                transaction.type().name());
        if (response.getErrorCode() == ERROR_CUSTOMER_NOT_EXIST) {
            throw new ResourceNotFoundException();
        }
        if (response.getErrorCode() == ERROR_INVALID_TRANSACTION) {
            throw new InvalidTransactionException();
        }
        return Balance.withBalanceAndLimit(response.getCustomerBalance(), response.getCustomerLimit());
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
