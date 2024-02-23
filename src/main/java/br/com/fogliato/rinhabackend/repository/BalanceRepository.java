package br.com.fogliato.rinhabackend.repository;

import br.com.fogliato.rinhabackend.entity.BalanceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends CrudRepository<BalanceEntity, Integer> {

    @Query("SELECT b FROM BalanceEntity b WHERE customerId = ?1 ORDER BY createdAt DESC LIMIT 1")
    BalanceEntity findLastBalance(int customerId);
}
