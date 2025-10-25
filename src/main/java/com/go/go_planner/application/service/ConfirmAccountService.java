package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.ConfirmAccountUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.StatusUsuario;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmAccountService implements ConfirmAccountUseCase {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void confirmAccount(ConfirmAccountCommand command) {
        Usuario usuario = usuarioRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (usuario.getStatus() == StatusUsuario.ATIVO) {
            throw new IllegalStateException("Esta conta já foi ativada.");
        }
        if (usuario.getDataExpiracaoCodigo().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Código de confirmação expirado.");
        }
        if (!usuario.getCodigoConfirmacao().equals(command.codigo())) {
            throw new IllegalArgumentException("Código de confirmação inválido.");
        }

        usuario.setStatus(StatusUsuario.ATIVO);
        usuario.setCodigoConfirmacao(null);
        usuario.setDataExpiracaoCodigo(null);

        usuarioRepository.save(usuario);
    }
}