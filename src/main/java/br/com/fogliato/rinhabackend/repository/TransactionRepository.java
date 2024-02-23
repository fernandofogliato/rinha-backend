package br.com.fogliato.rinhabackend.repository;

import br.com.fogliato.rinhabackend.entity.TransactionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE t.customerId = ?1 ORDER BY t.createdAt Desc LIMIT 10")
    List<TransactionEntity> findLastTransactions(long customerId);
}
