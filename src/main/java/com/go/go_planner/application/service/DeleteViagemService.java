// Em: com.go.go_planner.application.service.DeleteViagemService.java

package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.DeleteViagemUseCase;
import com.go.go_planner.application.port.out.ViagemRepository; // Sua porta de saída
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException; // Exceção para 403
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException; // Exceção para 404

@Service
@RequiredArgsConstructor
public class DeleteViagemService implements DeleteViagemUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public void deleteViagem(String viagemId, String userId) {
        // 1. Buscar a viagem no banco de dados
        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada com o ID: " + viagemId));

        if (!viagem.getCriadorViagemID().equals(userId)) {
            throw new AccessDeniedException("Usuário não autorizado a deletar esta viagem.");
        }

        viagemRepository.deleteById(viagemId);
    }
}