package br.com.fogliato.rinhabackend.repository;

import br.com.fogliato.rinhabackend.entity.CustomerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {

    @Modifying
    @Query("UPDATE CustomerEntity c SET c.balance = (c.balance + ?2) WHERE c.customerId = ?1")
    void updateBalance(long customerId, long amount);
}
