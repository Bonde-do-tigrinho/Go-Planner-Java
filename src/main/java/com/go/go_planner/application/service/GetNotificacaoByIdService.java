package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetNotificacaoByIdUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.domain.model.Notificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GetNotificacaoByIdService implements GetNotificacaoByIdUseCase {

    private final NotificacaoRepository notificacaoRepository;

    @Override
    public Notificacao execute(String notificacaoId, String usuarioLogadoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new NoSuchElementException("Notificação não encontrada."));

        // 2. SEGURANÇA: Verificar se a notificação pertence ao usuário logado
        if (!notificacao.getDestinatarioId().equals(usuarioLogadoId)) {
            throw new AccessDeniedException("Você não tem permissão para visualizar esta notificação.");
        }

        return notificacao;
    }
}
