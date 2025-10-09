package com.go.go_planner.application.port.service;

import com.go.go_planner.application.port.in.UpdateUserUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserUseCase {

    private final UsuarioRepository usuarioRepository;

    // A assinatura do método agora corresponde à interface.
    @Override
    public Usuario updateUser(UpdateUserCommand command) {
        // 1. Busca o usuário existente usando o ID do Command.
        Usuario usuarioParaAtualizar = usuarioRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para atualização."));

        if (StringUtils.hasText(command.nome())) {
            usuarioParaAtualizar.setNome(command.nome());
        }
        if( StringUtils.hasText(command.email())) {
            usuarioParaAtualizar.setEmail(command.email());
        }
        if (StringUtils.hasText(command.cpf())) {
            usuarioParaAtualizar.setCpf(command.cpf());
        }
        if (command.foto() != null) {
            usuarioParaAtualizar.setFoto(command.foto());
        }

        // 3. Salva o objeto modificado e retorna a versão atualizada.
        return usuarioRepository.save(usuarioParaAtualizar);
    }
}