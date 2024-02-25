package br.com.fogliato.rinhabackend.repository;

import br.com.fogliato.rinhabackend.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Integer> {

}
