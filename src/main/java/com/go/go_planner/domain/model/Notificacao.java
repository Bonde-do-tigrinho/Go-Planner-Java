package com.go.go_planner.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notificacoes")
public class Notificacao {

    @Id
    private String id;
    private String mensagem;
    private String destinatarioId; // ID do usuário que recebe a notificação
    private String referenciaId; // ID de referência (viagem, convite, etc.)
    private String dataHora;
    private boolean lida = false;
    private String viagemId; // ID da viagem associada, se aplicável
    private String remetenteId; // ID do remetente da notificação
    private TipoNotificacao tipo; // Tipo de notificação (convite, alerta, etc.)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Notificacao(String destinatarioId, TipoNotificacao tipo, String referenciaId, String mensagem) {
        this.destinatarioId = destinatarioId;
        this.tipo = tipo;
        this.referenciaId = referenciaId;
        this.mensagem = mensagem;
        this.lida = false;
        this.dataCriacao = LocalDateTime.now();
    }

}