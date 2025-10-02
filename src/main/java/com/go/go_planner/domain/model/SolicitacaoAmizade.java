package com.go.go_planner.domain.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "solicitacoes_viagem")
public class SolicitacaoAmizade {
    @Id
    private String id;
    private String solicitanteId;
    private String solicitadoId;
    private StatusSolicitacao status;
    private Date dataCriacao;

    public SolicitacaoAmizade(String solicitanteId, String solicitadoId, StatusSolicitacao status, Date dataCriacao) {
        this.solicitanteId = solicitanteId;
        this.solicitadoId = solicitadoId;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }
}
