package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.AceitarAmizadeUseCase;
import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepositoryPort;
import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.SolicitacaoAmizade;
import com.go.go_planner.domain.model.StatusSolicitacao;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AceitarAmizadeService implements AceitarAmizadeUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    // 1. Injete o repositório de solicitações que criamos
    private final SolicitacaoAmizadeRepositoryPort solicitacaoAmizadeRepositoryPort;

    // 2. A assinatura do método agora está CORRETA, implementando a interface
    @Override
    public void aceitarAmizade(AceitarAmizadeCommand command) {
        final String idUsuarioAtual = command.idUsuarioAtual();
        final String idAmigoAprovado = command.idAmigoAprovado();

        // 3. Encontra a solicitação PENDENTE entre os dois. A ordem não importa.
        // O aprovado é o 'solicitado', e o atual é quem aceita (originalmente o 'solicitado').
        // Assumimos que 'idAmigoAprovado' é quem enviou o pedido.
        SolicitacaoAmizade solicitacao = solicitacaoAmizadeRepositoryPort
                .findPendenteByParticipantes(idUsuarioAtual, idAmigoAprovado)
                .orElseThrow(() -> new IllegalStateException("Nenhuma solicitação de amizade pendente encontrada."));

        // Validação extra: o usuário que está aceitando deve ser o que foi solicitado.
        if (!solicitacao.getSolicitadoId().equals(idUsuarioAtual)) {
            throw new SecurityException("Ação não permitida. Você não pode aceitar este convite.");
        }

        // 4. Busca os dois objetos de usuário
        Usuario usuarioAtual = usuarioRepositoryPort.findById(idUsuarioAtual)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuarioAtual));

        Usuario amigoAprovado = usuarioRepositoryPort.findById(idAmigoAprovado)
                .orElseThrow(() -> new RuntimeException("Amigo não encontrado: " + idAmigoAprovado));

        // 5. ATUALIZA A LÓGICA: Adiciona os IDs na lista de amigos confirmados de cada um
        usuarioAtual.getAmigos().add(amigoAprovado.getId());
        amigoAprovado.getAmigos().add(usuarioAtual.getId());

        // 6. Atualiza o status da SOLICITAÇÃO para ACEITA
        solicitacao.setStatus(StatusSolicitacao.ACEITA);

        // 7. Salva todas as entidades modificadas
        usuarioRepositoryPort.save(usuarioAtual);
        usuarioRepositoryPort.save(amigoAprovado);
        solicitacaoAmizadeRepositoryPort.update(solicitacao);
    }
}