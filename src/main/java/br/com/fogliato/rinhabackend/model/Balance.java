package br.com.fogliato.rinhabackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record Balance(int total, @JsonProperty("data_extrato") Instant createdAt, @JsonProperty("limite") int limit) {

    public static Balance withBalanceAndLimit(int balance, int limit) {
        return new Balance(balance, Instant.now(), limit);
    }
}
