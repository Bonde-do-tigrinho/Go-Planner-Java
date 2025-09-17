package com.go.go_planner.domain.model;

import java.util.Date;
import java.util.List;

public class Viagem {

    private Long    id;
    private String  titulo;
    private String  localPartida;
    private String  localDestino;
    private Date    dataPartida;
    private Date    dataRetorno;
    private String  descricao;
    private Boolean favoritada;
    private String  imagem;
    private Usuario criadorViagemID; // ID do usuário que criou a viagem
    private List<Atividade> atividades;
    private List<Usuario>   participantes;

}
