package br.com.fogliato.rinhabackend.model;

import br.com.fogliato.rinhabackend.entity.BalanceEntity;
import br.com.fogliato.rinhabackend.entity.CustomerEntity;

import java.time.Instant;

public record Saldo(int total, Instant dataExtrato, int limite) {

    public static Saldo fromEntity(CustomerEntity customer, BalanceEntity balance) {
        return new Saldo(balance.getBalance(), Instant.now(), customer.getLimit());
    }
}
