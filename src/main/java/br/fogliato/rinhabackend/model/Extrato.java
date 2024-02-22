package br.fogliato.rinhabackend.model;

import java.util.List;

public record Extrato(Saldo saldo, List<Transacao> ultimasTransacoes) {
}
