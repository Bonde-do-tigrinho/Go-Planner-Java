package com.go.go_planner.domain.model;

public class Notificacoes {

    private Long id;
    private String tipo;
    private String mensagem;
    private String dataHora;
    private String status; // Lida ou Não Lida
    private String viagemId; // ID da viagem associada, se aplicável
    private String remetenteId; // ID do remetente da notificação

}
