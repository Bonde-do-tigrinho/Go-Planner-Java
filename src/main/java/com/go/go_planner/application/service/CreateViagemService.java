package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.CreateViagemUseCase;
import com.go.go_planner.application.port.out.ViagemRepository; // Supondo que você criou este repositório
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

        // 3. Preenchimento de campos gerados pelo sistema
        novaViagem.setCriadorViagemID(command.criadorId()); // ID seguro, vindo do token
        novaViagem.getParticipantesIds().add(command.criadorId()); // O criador é o primeiro participante

        // 4. Persistência
        return viagemRepository.save(novaViagem);
    }
}