package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.RecusarAmizadeUseCase;
import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepository;
import com.go.go_planner.domain.model.SolicitacaoAmizade;
import com.go.go_planner.domain.model.StatusSolicitacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecusarAmizadeService implements RecusarAmizadeUseCase {

    private final SolicitacaoAmizadeRepository solicitacaoAmizadeRepository;

    @Override
    public void recusarAmizade(RecusarAmizadeCommand command) {
        final String idUsuario = command.idUsuarioAtual();
        final String idAmigoRecusado = command.idUsuarioRecusado();

        SolicitacaoAmizade solicitacao = solicitacaoAmizadeRepository
                .findPendenteByParticipantes(idUsuario, idAmigoRecusado)
                .orElseThrow(() -> new IllegalStateException("Nenhuma solicitação de amizade pendente encontrada."));

        solicitacao.setStatus(StatusSolicitacao.RECUSADA);

        solicitacaoAmizadeRepository.save(solicitacao);

    }
}
