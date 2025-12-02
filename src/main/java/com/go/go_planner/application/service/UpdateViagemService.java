package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.UpdateViagemUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.domain.model.ViagemRole; // <--- Importante!
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateViagemService implements UpdateViagemUseCase {

    private final ViagemRepository viagemRepository;

    @Override
    public Viagem updateViagem(UpdateViagemCommand command) {
        Viagem viagemOriginal = viagemRepository.findById(command.viagemId())
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada: " + command.viagemId()));

        boolean isCriador = viagemOriginal.getCriadorViagemID().equals(command.userId());

        boolean isEditor = viagemOriginal.getParticipantes().stream()
                .anyMatch(p -> p.getUserId().equals(command.userId()) && p.getRole() == ViagemRole.EDITOR);

        if (!isCriador && !isEditor) {
            throw new AccessDeniedException("Você não tem permissão para editar esta viagem.");
        }

        Optional.ofNullable(command.titulo()).ifPresent(viagemOriginal::setTitulo);
        Optional.ofNullable(command.localPartida()).ifPresent(viagemOriginal::setLocalPartida);
        Optional.ofNullable(command.localDestino()).ifPresent(viagemOriginal::setLocalDestino);
        Optional.ofNullable(command.descricao()).ifPresent(viagemOriginal::setDescricao);
        Optional.ofNullable(command.imagem()).ifPresent(viagemOriginal::setImagem);

        LocalDateTime dataPartidaNova = command.dataPartida() != null ? command.dataPartida() : viagemOriginal.getDataPartida();
        LocalDateTime dataRetornoNova = command.dataRetorno() != null ? command.dataRetorno() : viagemOriginal.getDataRetorno();

        if (dataRetornoNova.isBefore(dataPartidaNova)) {
            throw new IllegalArgumentException("A data de retorno não pode ser anterior à data de partida.");
        }

        viagemOriginal.setDataPartida(dataPartidaNova);
        viagemOriginal.setDataRetorno(dataRetornoNova);

        return viagemRepository.save(viagemOriginal);
    }
}