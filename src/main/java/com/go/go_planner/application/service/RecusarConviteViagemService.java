package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.RecusarConviteViagemUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.application.port.out.SolicitacaoViagemRepository;
import com.go.go_planner.domain.model.Notificacao;
import com.go.go_planner.domain.model.SolicitacaoViagem;
import com.go.go_planner.domain.model.StatusSolicitacao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RecusarConviteViagemService implements RecusarConviteViagemUseCase {

    private final SolicitacaoViagemRepository solicitacaoViagemRepository;
    private final NotificacaoRepository notificacaoRepository;

    @Override
    public void execute(RecusarConviteCommand command) {
        SolicitacaoViagem solicitacao = solicitacaoViagemRepository.findById(command.solicitacaoId())
                .orElseThrow(() -> new NoSuchElementException("Convite não encontrado."));

        if (!solicitacao.getSolicitadoId().equals(command.usuarioLogadoId())) {
            throw new AccessDeniedException("Você não tem permissão para recusar este convite.");
        }

        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new IllegalStateException("Este convite já foi respondido.");
        }

        solicitacao.setStatus(StatusSolicitacao.RECUSADA);
        solicitacaoViagemRepository.save(solicitacao);

        marcarNotificacaoComoLida(solicitacao.getId());
    }

    private void marcarNotificacaoComoLida(String referenciaId) {
        List<Notificacao> notificacoes = notificacaoRepository.findByReferenciaId(referenciaId);
        for (Notificacao n : notificacoes) {
            if (!n.isLida()) {
                n.setLida(true);
                notificacaoRepository.save(n);
            }
        }
    }
}