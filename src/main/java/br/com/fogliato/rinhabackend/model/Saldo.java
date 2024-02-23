package br.com.fogliato.rinhabackend.model;

import br.com.fogliato.rinhabackend.entity.CustomerEntity;

import java.time.Instant;

public record Saldo(long total, Instant dataExtrato, long limite) {

    public static Saldo fromEntity(CustomerEntity entity) {
        return new Saldo(entity.getBalance(), Instant.now(), entity.getLimit());
    }
}
