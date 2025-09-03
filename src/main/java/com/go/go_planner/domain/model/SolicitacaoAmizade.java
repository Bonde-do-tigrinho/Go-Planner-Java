package com.go.go_planner.domain.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoAmizade {
    private String id; // ID do documento no Firestore
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
