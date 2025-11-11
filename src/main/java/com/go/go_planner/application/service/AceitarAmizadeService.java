package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.AceitarAmizadeUseCase;
import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepository;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.SolicitacaoAmizade;
import com.go.go_planner.domain.model.StatusSolicitacao;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AceitarAmizadeService implements AceitarAmizadeUseCase {

    private final UsuarioRepository usuarioRepositoryAdapter;
    private final SolicitacaoAmizadeRepository solicitacaoAmizadeRepository;

    @Override
    public void aceitarAmizade(AceitarAmizadeCommand command) {
        final String idUsuarioAtual = command.idUsuarioAtual();
        final String idAmigoAprovado = command.idAmigoAprovado();

        SolicitacaoAmizade solicitacao = solicitacaoAmizadeRepository
                .findPendenteByParticipantes(idUsuarioAtual, idAmigoAprovado)
                .orElseThrow(() -> new IllegalStateException("Nenhuma solicitação de amizade pendente encontrada."));

        Usuario usuarioAtual = usuarioRepositoryAdapter.findById(idUsuarioAtual)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuarioAtual));

        Usuario amigoAprovado = usuarioRepositoryAdapter.findById(idAmigoAprovado)
                .orElseThrow(() -> new RuntimeException("Amigo não encontrado: " + idAmigoAprovado));

        usuarioAtual.getAmigos().add(amigoAprovado.getId());
        amigoAprovado.getAmigos().add(usuarioAtual.getId());

        solicitacao.setStatus(StatusSolicitacao.ACEITA);

        usuarioRepositoryAdapter.save(usuarioAtual);
        usuarioRepositoryAdapter.save(amigoAprovado);
        solicitacaoAmizadeRepository.save(solicitacao);
    }
}