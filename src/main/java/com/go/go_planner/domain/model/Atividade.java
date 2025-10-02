package com.go.go_planner.domain.model;

import org.springframework.data.annotation.Id;
import java.util.Date;

public class Atividade {
    @Id
    private Long id;
    private String titulo;
    private Date data;
    private Boolean concluida;
}