package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.ConvidarParticipanteUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.application.port.out.SolicitacaoViagemRepository;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConvidarParticipanteService implements ConvidarParticipanteUseCase {

    private final ViagemRepository viagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final SolicitacaoViagemRepository solicitacaoViagemRepository;
    private final NotificacaoRepository notificacaoRepository;


    @Override
    public void convidar(ConvidarCommand command) {
        Viagem viagem = viagemRepository.findById(command.viagemId())
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada."));

        if (!viagem.getCriadorViagemID().equals(command.criadorId())) {
            throw new AccessDeniedException("Apenas o criador da viagem pode convidar participantes.");
        }

        Usuario convidado = usuarioRepository.findByEmail(command.emailConvidado())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o e-mail: " + command.emailConvidado()));

        boolean usuarioJaParticipa = viagem.getParticipantes().stream()
                .anyMatch(participante -> participante.getUserId().equals(convidado.getId()));

        if (usuarioJaParticipa) {
            throw new IllegalArgumentException("Este usuário já faz parte desta viagem.");
        }

        boolean convitePendente = solicitacaoViagemRepository.existsBySolicitadoIdAndIDviagemAndStatus(
                convidado.getId(),
                viagem.getId(),
                StatusSolicitacao.PENDENTE
        );

        if (convitePendente) {
            throw new IllegalStateException("Já existe um convite pendente para este usuário nesta viagem.");
        }


        SolicitacaoViagem solicitacao = new SolicitacaoViagem();
        solicitacao.setSolicitanteId(command.criadorId());
        solicitacao.setSolicitadoId(convidado.getId());
        solicitacao.setIDviagem(viagem.getId());
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setDataCriacao(new Date());

        SolicitacaoViagem solicitacaoSalva = solicitacaoViagemRepository.save(solicitacao);

        String mensagem = "Você foi convidado para participar da viagem: " + viagem.getTitulo();

        Notificacao novaNotificacao = Notificacao.builder() // Usando Builder para ficar mais limpo
                .destinatarioId(convidado.getId())
                .remetenteId(command.criadorId())
                .tipo(TipoNotificacao.SOLICITACAO_VIAGEM) // Certifique-se de ter esse Enum
                .referenciaId(solicitacaoSalva.getId()) // Link para a Solicitação de Viagem
                .viagemId(viagem.getId()) // Aqui podemos preencher o viagemId para facilitar o front
                .mensagem(mensagem)
                .lida(false)
                .build();

        notificacaoRepository.save(novaNotificacao);
    }

}