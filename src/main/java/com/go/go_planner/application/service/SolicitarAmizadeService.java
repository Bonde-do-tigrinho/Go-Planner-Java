package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.SolicitarAmizadeUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepository;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitarAmizadeService implements SolicitarAmizadeUseCase {

    private final UsuarioRepository usuarioRepository;
    private final SolicitacaoAmizadeRepository solicitacaoAmizadeRepository;
    private final NotificacaoRepository notificacaoRepository;

    @Override
    public void solicitarAmizade(SolicitarAmizadeCommand command) {
        final String idUsuarioAtual = command.idUsuarioAtual();
        final String idAmigoSolicitado = command.idAmigoSolicitado();

        if (idUsuarioAtual.equals(idAmigoSolicitado)) {
            throw new IllegalArgumentException("Um usuário não pode solicitar amizade a si mesmo.");
        }

        Usuario solicitante = usuarioRepository.findById(idUsuarioAtual)
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado: " + idUsuarioAtual));

        Usuario solicitado = usuarioRepository.findById(idAmigoSolicitado)
                .orElseThrow(() -> new RuntimeException("Usuário solicitado não encontrado: " + idAmigoSolicitado));

        if (solicitante.getAmigos().contains(idAmigoSolicitado)) {
            throw new IllegalStateException("Vocês já são amigos.");
        }

        if (solicitacaoAmizadeRepository.findPendenteByParticipantes(idUsuarioAtual, idAmigoSolicitado).isPresent()) {
            throw new IllegalStateException("Já existe uma solicitação de amizade pendente entre esses usuários.");
        }

        SolicitacaoAmizade novaSolicitacao = new SolicitacaoAmizade();
        novaSolicitacao.setSolicitanteId(idUsuarioAtual);
        novaSolicitacao.setSolicitadoId(idAmigoSolicitado);
        novaSolicitacao.setStatus(StatusSolicitacao.PENDENTE);

        SolicitacaoAmizade solicitacaoSalva = solicitacaoAmizadeRepository.save(novaSolicitacao);

        String mensagem = solicitante.getNome() + " enviou-lhe um pedido de amizade.";

        // --- ATUALIZAÇÃO AQUI ---
        Notificacao novaNotificacao = new Notificacao(
                solicitado.getId(),                 // Destinatário
                idUsuarioAtual,                     // Remetente (ADICIONADO)
                TipoNotificacao.SOLICITACAO_AMIZADE, // Tipo
                solicitacaoSalva.getId(),           // Referencia (ID da solicitação)
                mensagem                            // Mensagem
        );

        notificacaoRepository.save(novaNotificacao);
    }
}