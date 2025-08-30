package com.go.go_planner.application.port.in;

public interface AceitarAmizadeUseCase {

    void aceitarAmizade(AceitarAmizadeCommand command);

    class AceitarAmizadeCommand {
        private final String idUsuarioAtual;   // Quem está aceitando
        private final String idAmigoAprovado;  // O amigo que foi aceito

        public AceitarAmizadeCommand(String idUsuarioAtual, String idAmigoAprovado) {
            this.idUsuarioAtual = idUsuarioAtual;
            this.idAmigoAprovado = idAmigoAprovado;
        }

        // Getters
        public String getIdUsuarioAtual() { return idUsuarioAtual; }
        public String getIdAmigoAprovado() { return idAmigoAprovado; }
    }
}