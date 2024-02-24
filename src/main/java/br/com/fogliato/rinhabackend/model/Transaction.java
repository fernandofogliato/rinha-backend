package br.com.fogliato.rinhabackend.model;

import br.com.fogliato.rinhabackend.entity.TransactionEntity;
import br.com.fogliato.rinhabackend.type.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record Transaction(
        @NotNull(message = "Valor é obrigatório")
        @Min(value  = 1, message = "Informe um value positivo válido")
        @JsonProperty("valor")
        Integer value,

        @JsonProperty("tipo")
        TransactionType type,
        @NotBlank(message = "Descrição não pode ser nula/vazia")
        @Size(max = 10, message = "Máximo 10 caracteres")
        @JsonProperty("descricao")
        String description,

        @JsonProperty("realizada_em")
        Instant createdAt
) {

    public TransactionEntity toEntity(int customerId) {
        TransactionEntity entity = new TransactionEntity();
        entity.setCustomerId(customerId);
        entity.setType(type);
        entity.setDescription(description);
        entity.setValue(value);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public static Transaction fromEntity(TransactionEntity entity) {
        return new Transaction(entity.getValue(),
                entity.getType(),
                entity.getDescription(),
                entity.getCreatedAt().toInstant(ZoneOffset.UTC));
    }
}
