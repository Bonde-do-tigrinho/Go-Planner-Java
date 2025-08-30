package com.go.go_planner.application.port.in;

public interface SolicitarAmizadeUseCase {

    void solicitarAmizade(SolicitarAmizadeCommand command);

    class SolicitarAmizadeCommand {
        private final String idSolicitante; // quem está pedindo
        private final String idSolicitado; // para quem está pedindo

        public SolicitarAmizadeCommand(String idSolicitante, String idSolicitado) {
            if (idSolicitante.equals(idSolicitado)) {
                throw new IllegalArgumentException("Usuário não pode solicitar amizade a si mesmo.");
            }
            this.idSolicitante = idSolicitante;
            this.idSolicitado = idSolicitado;
        }

        public String getIdSolicitante() {
            return idSolicitante;
        }

        public String getIdSolicitado() {
            return idSolicitado;
        }
    }
}