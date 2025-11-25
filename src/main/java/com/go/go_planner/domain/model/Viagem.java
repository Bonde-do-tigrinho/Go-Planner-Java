package com.go.go_planner.domain.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "viagens")
public class Viagem {

    @Id
    private String    id;
    private String  titulo;
    private String  localPartida;
    private String  localDestino;
    private LocalDateTime dataPartida;
    private LocalDateTime    dataRetorno;
    private String  descricao;
    private String  imagem;
    private String criadorViagemID; // ID do usuário que criou a viagem

    private Boolean favoritada = false;
    private List<Atividade> atividades = new ArrayList<>();
    private List<Participante> participantes = new ArrayList<>();
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Participante {
        private String userId;
        private ViagemRole role;
    }

}