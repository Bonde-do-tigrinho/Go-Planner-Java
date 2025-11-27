package com.go.go_planner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  Atividade{
    private String id;
    private String titulo;
    private LocalDateTime dataHora;
    private Boolean concluida = false;
}