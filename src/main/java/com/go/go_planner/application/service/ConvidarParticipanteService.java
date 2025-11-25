package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.ConvidarParticipanteUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.domain.model.Viagem;
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

        // 3. Buscar o usuário que está sendo convidado
        // (Neste MVP, o usuário já precisa ter conta no App)
        Usuario convidado = usuarioRepository.findByEmail(command.emailConvidado())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o e-mail: " + command.emailConvidado()));

        // 4. Verificar se ele já não está na viagem
        if (viagem.getParticipantesIds().contains(convidado.getId())) {
            throw new IllegalArgumentException("Este usuário já é um participante desta viagem.");
        }

        // 5. Adicionar o ID dele na lista
        viagem.getParticipantesIds().add(convidado.getId());
        viagemRepository.save(viagem);

        // 6. Enviar E-mail de Notificação
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