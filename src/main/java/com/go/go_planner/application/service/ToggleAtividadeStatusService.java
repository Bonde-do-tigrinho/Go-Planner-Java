package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.ToggleAtividadeStatusUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Atividade;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.domain.model.ViagemRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ToggleAtividadeStatusService implements ToggleAtividadeStatusUseCase{

    private final ViagemRepository viagemRepository;

    @Override
    public Viagem execute(ToggleAtividadeCommand command) {
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

        // 2. VERIFICAÇÃO DE SEGURANÇA (Apenas o dono mexe na viagem)
        if (!viagem.getCriadorViagemID().equals(command.userId())) {
            throw new AccessDeniedException("Usuário não autorizado a modificar esta viagem.");
        }

        // 3. Encontrar a Atividade específica na lista
        Atividade atividadeAlvo = viagem.getAtividades().stream()
                .filter(a -> a.getId().equals(command.atividadeId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Atividade não encontrada com ID: " + command.atividadeId()));

        // 4. Inverter o status (Toggle)
        // Se era false vira true, se era true vira false
        boolean novoStatus = !Boolean.TRUE.equals(atividadeAlvo.getConcluida());
        atividadeAlvo.setConcluida(novoStatus);

        // 5. Salvar a Viagem inteira
        return viagemRepository.save(viagem);
    }
}
