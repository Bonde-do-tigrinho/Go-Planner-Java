package com.go.go_planner.domain.model;

import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Viagem {

    @Id
    private String    id;
    private String  titulo;
    private String  localPartida;
    private String  localDestino;
    private LocalDateTime dataPartida;
    private LocalDateTime    dataRetorno;
    private String  descricao;
    private Boolean favoritada;
    private String  imagem;
    private String criadorViagemID; // ID do usuário que criou a viagem
    private List<Atividade> atividades;
    private List<String>   participantes;
}