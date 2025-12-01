package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.AceitarConviteViagemUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.application.port.out.SolicitacaoViagemRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AceitarConviteViagemService implements AceitarConviteViagemUseCase {

    private final SolicitacaoViagemRepository solicitacaoViagemRepository;
    private final ViagemRepository viagemRepository;
    private final NotificacaoRepository notificacaoRepository;

    @Override
    public void execute(AceitarConviteCommand command) {
        SolicitacaoViagem solicitacao = solicitacaoViagemRepository.findById(command.solicitacaoId())
                .orElseThrow(() -> new NoSuchElementException("Convite não encontrado."));

        if (!solicitacao.getSolicitadoId().equals(command.usuarioLogadoId())) {
            throw new AccessDeniedException("Você não tem permissão para aceitar este convite.");
        }

        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new IllegalStateException("Este convite já foi respondido.");
        }

        Viagem viagem = viagemRepository.findById(solicitacao.getViagemId())
                .orElseThrow(() -> new NoSuchElementException("A viagem deste convite não existe mais."));

        boolean jaEstaNaViagem = viagem.getParticipantes().stream()
                .anyMatch(p -> p.getUserId().equals(command.usuarioLogadoId()));

        if (!jaEstaNaViagem) {
            viagem.getParticipantes().add(new Viagem.Participante(
                    command.usuarioLogadoId(),
                    ViagemRole.LEITOR
            ));
            viagemRepository.save(viagem);
        }

        solicitacao.setStatus(StatusSolicitacao.ACEITA);
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