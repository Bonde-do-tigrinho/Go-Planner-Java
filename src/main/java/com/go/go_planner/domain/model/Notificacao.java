package com.go.go_planner.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notificacoes")
public class Notificacao {

    @Id
    private String id;
    private String mensagem;
    private String destinatarioId;
    private String referenciaId;
    private String dataHora;
    private boolean lida = false;
    private String viagemId;
    private String remetenteId;
    private TipoNotificacao tipo;
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Notificacao(String destinatarioId, String remetenteId, TipoNotificacao tipo, String referenciaId, String mensagem) {
        this.destinatarioId = destinatarioId;
        this.remetenteId = remetenteId;
        this.tipo = tipo;
        this.referenciaId = referenciaId;
        this.mensagem = mensagem;
        this.lida = false;
        this.dataCriacao = LocalDateTime.now();
    }

}