package com.go.go_planner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // Essencial para o Firebase/Firestore
@AllArgsConstructor // Útil para testes, mas vamos garantir que não cause problemas.
public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String foto;

    // 1. Apenas declare a lista, não inicialize aqui.
    private List<String> amigos;

    // Proativamente, fazendo o mesmo para notificações.
    private List<Notificacoes> notificacoes;

    public List<String> getAmigos() {
        // Se a lista de amigos for nula por QUALQUER motivo...
        if (this.amigos == null) {
            // ...nós a inicializamos com uma nova lista vazia.
            this.amigos = new ArrayList<>();
        }
        // Agora, temos a GARANTIA de que este método NUNCA retornará null.
        return this.amigos;
    }

    public List<Notificacoes> getNotificacoes() {
        if (this.notificacoes == null) {
            this.notificacoes = new ArrayList<>();
        }
        return this.notificacoes;
    }
}