package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.CreateViagemUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateViagemService implements CreateViagemUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public Viagem createViagem(CreateViagemCommand command) {
        // 1. Validação de negócio
        if (command.dataRetorno().isBefore(command.dataPartida())) {
            throw new IllegalArgumentException("A data de retorno não pode ser anterior à data de partida.");
        }

        // 2. Mapeamento do Command para o objeto de Domínio
        Viagem novaViagem = new Viagem();
        novaViagem.setTitulo(command.titulo());
        novaViagem.setLocalPartida(command.localPartida());
        novaViagem.setLocalDestino(command.localDestino());
        novaViagem.setDataPartida(command.dataPartida());
        novaViagem.setDataRetorno(command.dataRetorno());
        novaViagem.setDescricao(command.descricao());
        novaViagem.setImagem(command.imagem());

        novaViagem.setCriadorViagemID(command.criadorId());
        novaViagem.getParticipantesIds().add(command.criadorId());

        return viagemRepository.save(novaViagem);
    }
}