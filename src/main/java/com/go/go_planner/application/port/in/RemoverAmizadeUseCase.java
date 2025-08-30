package com.go.go_planner.application.port.in;

public interface RemoverAmizadeUseCase {

    void removerAmizade(RemoverAmizadeCommand command);

    class RemoverAmizadeCommand {
        private final String idUsuarioAtual;
        private final String idAmigoParaRemover;

        public RemoverAmizadeCommand(String idUsuarioAtual, String idAmigoParaRemover) {
            this.idUsuarioAtual = idUsuarioAtual;
            this.idAmigoParaRemover = idAmigoParaRemover;
        }

        public String getIdUsuarioAtual() {
            return idUsuarioAtual;
        }

        public String getIdAmigoParaRemover() {
            return idAmigoParaRemover;
        }
    }
}