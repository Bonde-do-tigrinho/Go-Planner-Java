package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.SolicitacaoAmizade;

import java.util.Optional;

public interface SolicitacaoAmizadeRepositoryPort {

    SolicitacaoAmizade save(SolicitacaoAmizade solicitacao);
    void update(SolicitacaoAmizade solicitacao);
    Optional<SolicitacaoAmizade> findPendenteByParticipantes(String idUsuario1, String idUsuario2);

    default boolean existeSolicitacaoPendente(String idUsuario1, String idUsuario2) {
        return findPendenteByParticipantes(idUsuario1, idUsuario2).isPresent();
    }
}
