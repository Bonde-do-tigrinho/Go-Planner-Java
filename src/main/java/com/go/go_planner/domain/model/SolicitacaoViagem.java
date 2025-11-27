package com.go.go_planner.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "solicitacoes_viagem")
public class SolicitacaoViagem {

    @Id
    private String id;
    private String solicitanteId;
    private String solicitadoId;
    private String IDviagem;
    private StatusSolicitacao status;
    private Date dataCriacao;

}
