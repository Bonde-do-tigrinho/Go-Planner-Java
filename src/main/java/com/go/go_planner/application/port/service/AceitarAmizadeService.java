package com.go.go_planner.application.port.service;

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

    // 👇 USE AS NOVAS INTERFACES AQUI 👇
    private final UsuarioRepository usuarioRepositoryAdapter;
    private final SolicitacaoAmizadeRepository solicitacaoAmizadeRepository;

    @Override
    public void aceitarAmizade(AceitarAmizadeCommand command) {
        final String idUsuarioAtual = command.idUsuarioAtual();
        final String idAmigoAprovado = command.idAmigoAprovado();

        SolicitacaoAmizade solicitacao = solicitacaoAmizadeRepository
                // Use o novo método do MongoRepository
                .findPendenteByParticipantes(idUsuarioAtual, idAmigoAprovado)
                .orElseThrow(() -> new IllegalStateException("Nenhuma solicitação de amizade pendente encontrada."));

        // Busca os dois objetos de usuário usando o novo repositório
        Usuario usuarioAtual = usuarioRepositoryAdapter.findById(idUsuarioAtual)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuarioAtual));

        Usuario amigoAprovado = usuarioRepositoryAdapter.findById(idAmigoAprovado)
                .orElseThrow(() -> new RuntimeException("Amigo não encontrado: " + idAmigoAprovado));

        // Adiciona os IDs na lista de amigos
        usuarioAtual.getAmigos().add(amigoAprovado.getId());
        amigoAprovado.getAmigos().add(usuarioAtual.getId());

        // Atualiza o status da solicitação
        solicitacao.setStatus(StatusSolicitacao.ACEITA);

        // Salva as entidades modificadas
        usuarioRepositoryAdapter.save(usuarioAtual);
        usuarioRepositoryAdapter.save(amigoAprovado);
        solicitacaoAmizadeRepository.save(solicitacao); // .save() também atualiza
    }
}