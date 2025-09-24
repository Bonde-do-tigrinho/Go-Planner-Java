package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.SolicitarAmizadeUseCase;
import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepositoryPort;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.SolicitacaoAmizade;
import com.go.go_planner.domain.model.StatusSolicitacao;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class SolicitarAmizadeService implements SolicitarAmizadeUseCase {

    @Autowired
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    @Autowired
    private final SolicitacaoAmizadeRepositoryPort solicitacaoAmizadeRepositoryPort;

    @Override
    public void solicitarAmizade(SolicitarAmizadeCommand command) {

        final String idUsuarioAtual = command.idUsuarioAtual();
        final String idAmigoSolicitado = command.idAmigoSolicitado();

        if (idUsuarioAtual.equals(idAmigoSolicitado)) {
            throw new IllegalArgumentException("Um usuário não pode solicitar amizade a si mesmo.");
        }

        Usuario solicitante = usuarioRepositoryPort.findById(idUsuarioAtual)
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado: " + idUsuarioAtual));

        usuarioRepositoryPort.findById(idAmigoSolicitado)
                .orElseThrow(() -> new RuntimeException("Usuário solicitado não encontrado: " + idAmigoSolicitado));

//        if (solicitante.getAmigos().contains(idAmigoSolicitado)) {
//            throw new IllegalStateException("Vocês já são amigos.");
//        }

        if (solicitacaoAmizadeRepositoryPort.existeSolicitacaoPendente(idUsuarioAtual, idAmigoSolicitado)) {
            throw new IllegalStateException("Já existe uma solicitação de amizade pendente entre esses usuários.");
        }

        SolicitacaoAmizade novaSolicitacao = new SolicitacaoAmizade(
                idUsuarioAtual,
                idAmigoSolicitado,
                StatusSolicitacao.PENDENTE,
                new Date());

        solicitacaoAmizadeRepositoryPort.save(novaSolicitacao);
    }
}