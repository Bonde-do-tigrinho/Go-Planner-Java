package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.AdicionarAtividadeUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Atividade;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.domain.model.ViagemRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdicionarAtividadeService implements AdicionarAtividadeUseCase {

    private final ViagemRepository viagemRepository;
    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Viagem addAtividade(AddAtividadeCommand command) throws AccessDeniedException {
        // 1. Buscar a viagem
        Viagem viagem = viagemRepository.findById(command.viagemId())
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada com o ID: " + command.viagemId()));

        boolean isCriador = viagem.getCriadorViagemID().equals(command.userId());

        // Verifica se é participante com role EDITOR
        boolean isEditor = viagem.getParticipantes().stream()
                .anyMatch(p -> p.getUserId().equals(command.userId()) && p.getRole() == ViagemRole.EDITOR);

        if (!isCriador && !isEditor) {
            throw new AccessDeniedException("Você não tem permissão para adicionar atividades.");
        }

        // 2. VERIFICAÇÃO DE SEGURANÇA (Só o dono pode adicionar)
        if (!viagem.getCriadorViagemID().equals(command.userId())) {
            throw new AccessDeniedException("Usuário não autorizado a adicionar atividades a esta viagem.");
        }

        // 3. REGRA DE NEGÓCIO: Validar datas
        // A atividade não pode ser antes do início nem depois do fim da viagem
        if (command.dataHora().isBefore(viagem.getDataPartida()) ||
                command.dataHora().isAfter(viagem.getDataRetorno())) {
            throw new IllegalArgumentException("A data da atividade deve estar dentro do período da viagem.");
        }

        Atividade novaAtividade = new Atividade();
        novaAtividade.setId(UUID.randomUUID().toString());
        novaAtividade.setTitulo(command.titulo());
        novaAtividade.setDataHora(command.dataHora()); // Passa direto, sem conversão!
        novaAtividade.setConcluida(false);

        viagem.getAtividades().add(novaAtividade);

        return viagemRepository.save(viagem);
    }

}