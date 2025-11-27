package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.UpdateViagemUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Viagem;
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

        if (!viagemOriginal.getCriadorViagemID().equals(command.userId())) {
            throw new AccessDeniedException("Usuário não autorizado a modificar esta viagem.");
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

        // 5. Salvar a entidade atualizada
        return viagemRepository.save(viagemOriginal);
    }
}