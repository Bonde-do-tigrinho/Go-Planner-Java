package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.CreateViagemUseCase;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Atividade;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.domain.model.ViagemRole; // ⚠️ Importante importar o Enum
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

        // 👇 CORREÇÃO AQUI 👇
        // Criamos um objeto Participante com o ID do criador e o papel EDITOR
        novaViagem.getParticipantes().add(
                new Viagem.Participante(command.criadorId(), ViagemRole.EDITOR)
        );

        // Mapeamento das Atividades (Isso já estava correto no seu código)
        if (command.atividades() != null && !command.atividades().isEmpty()) {
            List<Atividade> listaDeAtividades = command.atividades().stream()
                    .map(dto -> new Atividade(
                            UUID.randomUUID().toString(), // Gera ID único para atividade
                            dto.titulo(),
                            dto.dataHora(),
                            false
                    ))
                    .collect(Collectors.toList());
            novaViagem.setAtividades(listaDeAtividades);
        }

        // Persiste a viagem
        Viagem viagemSalva = viagemRepository.save(novaViagem);

        // (Opcional) Se você quiser processar os convites por e-mail aqui também,
        // precisaria reinserir a lógica de chamar o UsuarioRepository, etc.
        // Mas para criar a viagem, o código acima já basta.

        return viagemSalva;
    }
}