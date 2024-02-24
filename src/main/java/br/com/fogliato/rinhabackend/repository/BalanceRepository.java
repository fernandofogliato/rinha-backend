package br.com.fogliato.rinhabackend.repository;

import br.com.fogliato.rinhabackend.entity.BalanceEntity;
import br.com.fogliato.rinhabackend.model.LastBalance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends CrudRepository<BalanceEntity, Integer> {

    @Query("SELECT new br.com.fogliato.rinhabackend.model.LastBalance(b.balance, c.limit) " +
           "FROM BalanceEntity b " +
           "INNER JOIN b.customer c " +
           "WHERE b.customer.customerId = ?1 " +
           "ORDER BY b.createdAt DESC LIMIT 1")
    Optional<LastBalance> findLastBalance(int customerId);
}
