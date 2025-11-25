package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.DeleteAtividadeUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.domain.model.ViagemRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DeleteAtividadeService implements DeleteAtividadeUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public void deleteAtividade(DeleteAtividadeCommand command) {
        // 1. Buscar a Viagem
        Viagem viagem = viagemRepository.findById(command.viagemId())
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada."));

        boolean isCriador = viagem.getCriadorViagemID().equals(command.userId());

        // Verifica se é participante com role EDITOR
        boolean isEditor = viagem.getParticipantes().stream()
                .anyMatch(p -> p.getUserId().equals(command.userId()) && p.getRole() == ViagemRole.EDITOR);

        if (!isCriador && !isEditor) {
            throw new AccessDeniedException("Você não tem permissão para adicionar atividades.");
        }

        // 2. VERIFICAÇÃO DE SEGURANÇA (Só o dono pode modificar a viagem)
        if (!viagem.getCriadorViagemID().equals(command.userId())) {
            throw new AccessDeniedException("Usuário não autorizado a modificar esta viagem.");
        }

        // 3. Deletar a Atividade
        // Usamos removeIf para remover o item da lista cujo ID de atividade corresponde
        boolean atividadeRemovida = viagem.getAtividades().removeIf(
                atividade -> atividade.getId().equals(command.atividadeId())
        );

        if (!atividadeRemovida) {
            // Se o ID da atividade não foi encontrado na lista da viagem, lançamos 404
            throw new NoSuchElementException("Atividade não encontrada na viagem: " + command.viagemId());
        }

        // 4. Salvar o documento Viagem atualizado de volta no banco
        viagemRepository.save(viagem);
    }
}
