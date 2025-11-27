package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.CreateViagemUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.application.port.out.SolicitacaoViagemRepository;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateViagemService implements CreateViagemUseCase {

    private final ViagemRepository viagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitacaoViagemRepository solicitacaoViagemRepository;
    private final NotificacaoRepository notificacaoRepository;

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

        novaViagem.getParticipantes().add(
                new Viagem.Participante(command.criadorId(), ViagemRole.LEITOR)
        );

        if (command.atividades() != null && !command.atividades().isEmpty()) {
            List<Atividade> listaDeAtividades = command.atividades().stream()
                    .map(dto -> new Atividade(
                            UUID.randomUUID().toString(),
                            dto.titulo(),
                            dto.dataHora(),
                            false
                    ))
                    .collect(Collectors.toList());
            novaViagem.setAtividades(listaDeAtividades);
        }

        Viagem viagemSalva = viagemRepository.save(novaViagem);

        if (command.emailsParaConvidar() != null && !command.emailsParaConvidar().isEmpty()) {
            processarConvites(viagemSalva, command.emailsParaConvidar(), command.criadorId());
        }

        return viagemSalva;
    }

    private void processarConvites(Viagem viagem, List<String> emails, String criadorId) {
        for (String email : emails) {
            // Busca o usuário pelo e-mail
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isEmpty()) {
                // Decisão de negócio: Se o usuário não existe no app, ignoramos ou logamos?
                // Por enquanto vamos apenas ignorar para não quebrar a criação da viagem.
                continue;
            }

            Usuario convidado = usuarioOpt.get();

            // Evita convidar a si mesmo ou duplicatas (se o frontend mandar errado)
            if (convidado.getId().equals(criadorId)) continue;

            // 1. Criar Solicitação de Viagem
            SolicitacaoViagem solicitacao = new SolicitacaoViagem();
            solicitacao.setSolicitanteId(criadorId);
            solicitacao.setSolicitadoId(convidado.getId());
            solicitacao.setIDviagem(viagem.getId());
            solicitacao.setStatus(StatusSolicitacao.PENDENTE);
            solicitacao.setDataCriacao(new Date());

            SolicitacaoViagem solicitacaoSalva = solicitacaoViagemRepository.save(solicitacao);

            String mensagem = "Você foi convidado para participar da viagem: " + viagem.getTitulo();

            Notificacao notificacao = Notificacao.builder()
                    .destinatarioId(convidado.getId())
                    .remetenteId(criadorId)
                    .tipo(TipoNotificacao.SOLICITACAO_VIAGEM)
                    .referenciaId(solicitacaoSalva.getId()) // Link para a solicitação
                    .viagemId(viagem.getId())
                    .mensagem(mensagem)
                    .lida(false)
                    .build();

            notificacaoRepository.save(notificacao);

        }
    }
}