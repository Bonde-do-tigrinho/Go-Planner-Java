package com.go.go_planner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter // <--- Essencial para o .getId() funcionar
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Atividade {

    private String id;
    private String titulo;
    private LocalDateTime dataAtividade;
}