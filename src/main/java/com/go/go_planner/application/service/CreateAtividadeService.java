package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.CreateAtividadeUseCase;
import com.go.go_planner.application.port.in.CreateAtividadeUseCase.CreateAtividadeCommand; // Import do record
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Atividade;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAtividadeService implements CreateAtividadeUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public String createAtividade(CreateAtividadeCommand command) {

        Viagem viagem = viagemRepository.findById(command.viagemId())
                .orElseThrow(() -> new IllegalArgumentException("Viagem não encontrada com ID: " + command.viagemId()));

        if (command.dataAtividade().isBefore(viagem.getDataPartida()) ||
                command.dataAtividade().isAfter(viagem.getDataRetorno())) {
            throw new IllegalArgumentException("A data da atividade deve estar compreendida entre a data de partida e retorno da viagem.");
        }

        Atividade novaAtividade = new Atividade(
        );

        viagem.getAtividades().add(novaAtividade);

        viagemRepository.save(viagem);

        return novaAtividade.getId();
    }
}