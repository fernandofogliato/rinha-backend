package br.fogliato.rinhabackend.model;

import br.fogliato.rinhabackend.entity.TransactionEntity;
import br.fogliato.rinhabackend.type.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public record Transacao(
        @NotNull(message = "Valor é obrigatório")
        Long valor,
        TransactionType tipo,
        @NotBlank(message = "Descrição não pode ser nula/vazia")
        @Size(max = 10, message = "Máximo 10 caracteres")
        String descricao,
        Instant realizadaEm
) {

    public TransactionEntity toEntity(long customerId) {
        TransactionEntity entity = new TransactionEntity();
        entity.setCustomerId(customerId);
        entity.setType(tipo);
        entity.setDescription(descricao);
        entity.setValue(valor);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public static Transacao fromEntity(TransactionEntity entity) {
        return new Transacao(entity.getValue(),
                entity.getType(),
                entity.getDescription(),
                entity.getCreatedAt().toInstant(ZoneOffset.UTC));
    }
}
