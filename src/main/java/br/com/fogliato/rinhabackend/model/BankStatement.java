package br.com.fogliato.rinhabackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BankStatement(@JsonProperty("saldo") Balance balance, @JsonProperty("ultimas_transacoes") List<Transaction> lastTransactions) {
}
