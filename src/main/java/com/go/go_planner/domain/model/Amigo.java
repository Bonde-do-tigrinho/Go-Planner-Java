package com.go.go_planner.domain.model;

public class Amigo {

    private String idAmigo; // ID do usuário que é o amigo
    private StatusAmizade status;

    public enum StatusAmizade {
        PENDENTE,
        ACEITO,
        RECUSADO,
        BLOQUEADO
    }

    // Construtores
    public Amigo() {}

    public Amigo(String idAmigo, StatusAmizade status) {
        this.idAmigo = idAmigo;
        this.status = status;
    }

    // Getters e Setters
    public String getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(String idAmigo) {
        this.idAmigo = idAmigo;
    }

    public StatusAmizade getStatus() {
        return status;
    }

    public void setStatus(StatusAmizade status) {
        this.status = status;
    }
}