package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.SolicitarAmizadeUseCase;
import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepositoryPort;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.SolicitacaoAmizade;
import com.go.go_planner.domain.model.StatusSolicitacao;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class SolicitarAmizadeService implements SolicitarAmizadeUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final SolicitacaoAmizadeRepositoryPort solicitacaoAmizadeRepositoryPort;

    @Override
    public void solicitarAmizade(String idUsuarioAtual, String idAmigoSolicitado) {
        // 1. Validações Iniciais
        if (idUsuarioAtual.equals(idAmigoSolicitado)) {
            throw new IllegalArgumentException("Um usuário не pode solicitar amizade a si mesmo.");
        }

        Usuario solicitante = usuarioRepositoryPort.findById(idUsuarioAtual)
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado: " + idUsuarioAtual));

        Usuario solicitado = usuarioRepositoryPort.findById(idAmigoSolicitado)
                .orElseThrow(() -> new RuntimeException("Usuário solicitado não encontrado: " + idAmigoSolicitado));

        // 2. VERIFICAR se já são amigos ou se já existe uma solicitação pendente
        if (solicitante.getAmigos().contains(idAmigoSolicitado)) {
            throw new IllegalStateException("Vocês já são amigos.");
        }

        // Esta verificação é crucial para não criar solicitações duplicadas
        if (solicitacaoAmizadeRepositoryPort.existeSolicitacaoPendente(idUsuarioAtual, idAmigoSolicitado)) {
            throw new IllegalStateException("Já existe uma solicitação de amizade pendente entre esses usuários.");
        }

        // 3. Criar a nova Solicitação de Amizade
        SolicitacaoAmizade novaSolicitacao = new SolicitacaoAmizade();
        novaSolicitacao.setSolicitanteId(idUsuarioAtual);
        novaSolicitacao.setSolicitadoId(idAmigoSolicitado);
        novaSolicitacao.setStatus(StatusSolicitacao.PENDENTE);
        novaSolicitacao.setDataCriacao(new Date()); // Data atual

        // 4. Salvar a solicitação no banco de dados
        solicitacaoAmizadeRepositoryPort.save(novaSolicitacao);

        // 5. (Opcional, mas recomendado) Criar uma notificação para o usuário solicitado
        // notifiacaoService.criarNotificacaoDeAmizade(solicitante, solicitado);
    }
}