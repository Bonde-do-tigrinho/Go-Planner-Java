package com.go.go_planner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class SolicitacaoAmizade {
    private String id; // ID do documento no Firestore
    private String solicitanteId;
    private String solicitadoId;
    private StatusSolicitacao status;
    private Date dataCriacao;
}
