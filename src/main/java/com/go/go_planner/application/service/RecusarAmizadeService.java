package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.RecusarAmizadeUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepository;
import com.go.go_planner.domain.model.Notificacao;
import com.go.go_planner.domain.model.SolicitacaoAmizade;
import com.go.go_planner.domain.model.StatusSolicitacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecusarAmizadeService implements RecusarAmizadeUseCase {

    private final SolicitacaoAmizadeRepository solicitacaoAmizadeRepository;
    private final NotificacaoRepository notificacaoRepository; // <--- 1. INJETAR O REPOSITÓRIO

    @Override
    public void recusarAmizade(RecusarAmizadeCommand command) {
        final String idUsuario = command.idUsuarioAtual();
        final String idAmigoRecusado = command.idUsuarioRecusado();

        SolicitacaoAmizade solicitacao = solicitacaoAmizadeRepository
                .findPendenteByParticipantes(idUsuario, idAmigoRecusado)
                .orElseThrow(() -> new IllegalStateException("Nenhuma solicitação de amizade pendente encontrada."));

        solicitacao.setStatus(StatusSolicitacao.RECUSADA);

        solicitacaoAmizadeRepository.save(solicitacao);

        marcarNotificacaoComoLida(solicitacao.getId());
    }

    private void marcarNotificacaoComoLida(String referenciaId) {
        List<Notificacao> notificacoes = notificacaoRepository.findByReferenciaId(referenciaId);

        for (Notificacao notificacao : notificacoes) {
            if (!notificacao.isLida()) {
                notificacao.setLida(true);
                notificacaoRepository.save(notificacao);
            }
        }
    }
}
