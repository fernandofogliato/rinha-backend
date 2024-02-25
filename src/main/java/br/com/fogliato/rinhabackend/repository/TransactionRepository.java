package br.com.fogliato.rinhabackend.repository;

import br.com.fogliato.rinhabackend.entity.TransactionEntity;
import br.com.fogliato.rinhabackend.model.UpdatedBalance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Integer> {

    @Query("SELECT t FROM TransactionEntity t WHERE t.customerId = ?1 ORDER BY t.createdAt Desc LIMIT 10")
    List<TransactionEntity> findLastTransactions(long customerId);

    @Query(value = "SELECT customer_limit AS customerLimit, customer_balance AS customerBalance, error_code AS errorCode " +
                   "FROM process_transaction(:p_customer_id, :p_transaction_value, :p_description, :p_type)", nativeQuery = true)
    UpdatedBalance addTransaction(@Param("p_customer_id") int customerId,
                                  @Param("p_transaction_value") int transactionValue,
                                  @Param("p_description") String description,
                                  @Param("p_type") String type);
}
