package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.CreateViagemUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Atividade;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateViagemService implements CreateViagemUseCase {

    private final ViagemRepository viagemRepository;
    private final EmailService emailService;


    @Override
    public Viagem createViagem(CreateViagemCommand command) {
        if (command.dataRetorno().isBefore(command.dataPartida())) {
            throw new IllegalArgumentException("A data de retorno não pode ser anterior à data de partida.");
        }

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

        if (command.atividades() != null && !command.atividades().isEmpty()) {
            List<Atividade> listaDeAtividades = command.atividades().stream()
                    .map(dto ->
                            new Atividade(UUID.randomUUID().toString(),dto.titulo(), dto.dataHora(), false))
                    .collect(Collectors.toList());
            novaViagem.setAtividades(listaDeAtividades);
        }

        // 5. Persiste a viagem (Necessário para ter o ID da viagem)
        Viagem viagemSalva = viagemRepository.save(novaViagem);


        return viagemSalva;
    }
}