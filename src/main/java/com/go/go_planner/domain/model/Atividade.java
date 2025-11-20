package com.go.go_planner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  Atividade{
    private String titulo;
    private LocalDateTime dataHora;
    private Boolean concluida = false;
}