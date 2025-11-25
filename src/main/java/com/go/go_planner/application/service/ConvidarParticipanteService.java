package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.ConvidarParticipanteUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.domain.model.ViagemRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConvidarParticipanteService implements ConvidarParticipanteUseCase {

    private final ViagemRepository viagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService; // Seu serviço do Brevo

    @Override
    public void convidar(ConvidarCommand command) {
        // 1. Buscar a Viagem
        Viagem viagem = viagemRepository.findById(command.viagemId())
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada."));

        // 2. SEGURANÇA: Só o dono pode convidar
        if (!viagem.getCriadorViagemID().equals(command.criadorId())) {
            throw new AccessDeniedException("Apenas o criador da viagem pode convidar participantes.");
        }

        // 3. Buscar o usuário
        Usuario convidado = usuarioRepository.findByEmail(command.emailConvidado())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o e-mail: " + command.emailConvidado()));

        // 4. CORREÇÃO AQUI: Verificar se ele já está na lista (usando Stream)
        boolean usuarioJaParticipa = viagem.getParticipantes().stream()
                .anyMatch(participante -> participante.getUserId().equals(convidado.getId()));

        if (usuarioJaParticipa) {
            throw new IllegalArgumentException("Este usuário já é um participante desta viagem.");
        }

        // 5. CORREÇÃO AQUI: Criar o objeto Participante e adicionar
        // Vamos definir 'LEITOR' como padrão. Depois o dono pode mudar para EDITOR se quiser.
        Viagem.Participante novoParticipante = new Viagem.Participante(
                convidado.getId(),
                ViagemRole.LEITOR
        );

        viagem.getParticipantes().add(novoParticipante);

        viagemRepository.save(viagem);

        // 6. Enviar E-mail
        enviarEmailBoasVindas(convidado.getEmail(), viagem.getTitulo(), viagem.getLocalDestino());
    }

    private void enviarEmailBoasVindas(String email, String tituloViagem, String destino) {
        String assunto = "Convite de Viagem: " + tituloViagem;
        String corpo = String.format(
                "Olá! Você foi adicionado como participante da viagem '%s' para %s no Go-Planner. " +
                        "Acesse o aplicativo para ver os detalhes.",
                tituloViagem, destino
        );
        emailService.sendSimpleMessage(email, assunto, corpo);
    }
}