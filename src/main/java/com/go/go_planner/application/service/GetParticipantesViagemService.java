package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetParticipantesViagemUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.domain.model.Viagem;
import com.go.go_planner.infrastructure.config.adapter.in.web.dto.ParticipanteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetParticipantesViagemService implements GetParticipantesViagemUseCase {
    private final ViagemRepository viagemRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<ParticipanteResponseDTO> execute(String viagemId, String usuarioLogadoId) {
        // 1. Buscar a Viagem
        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada."));

        // 2. SEGURANÇA: Verificar se quem está pedindo faz parte da viagem
        boolean isParticipante = viagem.getParticipantes().stream()
                .anyMatch(p -> p.getUserId().equals(usuarioLogadoId));

        if (!isParticipante) {
            throw new AccessDeniedException("Você não tem permissão para ver os participantes desta viagem.");
        }

        // 3. Extrair os IDs dos usuários da lista de participantes
        List<String> userIds = viagem.getParticipantes().stream()
                .map(Viagem.Participante::getUserId)
                .toList();

        // 4. Buscar os dados completos desses usuários no banco
        List<Usuario> usuariosDetalhados = usuarioRepository.findAllById(userIds);

        // 5. Otimização: Criar um Map para busca rápida (ID -> Usuario)
        Map<String, Usuario> usuarioMap = usuariosDetalhados.stream()
                .collect(Collectors.toMap(Usuario::getId, Function.identity()));

        // 6. Montar o DTO combinando (Dados do Usuário + Role da Viagem)
        return viagem.getParticipantes().stream()
                .map(participante -> {
                    Usuario user = usuarioMap.get(participante.getUserId());

                    // Se por algum motivo o usuário foi deletado do banco, tratamos aqui
                    if (user == null) return null;

                    return new ParticipanteResponseDTO(
                            user.getId(),
                            user.getNome(),
                            user.getEmail(),
                            user.getFoto(),
                            participante.getRole() // Pega o papel (EDITOR/LEITOR) da viagem
                    );
                })
                .filter(dto -> dto != null) // Remove nulos caso algum usuário tenha sido excluído
                .toList();
    }
}
