package com.go.go_planner.domain.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class SolicitacaoViagem {

    @Id
    private String id;
    private String solicitanteId;
    private String solicitadoId;
    private String IDviagem;
    private StatusSolicitacao status;
    private Date dataCriacao;

}
