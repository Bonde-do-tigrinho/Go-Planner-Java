package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.CreateUserUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.domain.model.StatusUsuario;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public Usuario createUser(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        String encodedPassword = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encodedPassword);

        String codigo = new DecimalFormat("000000").format(new Random().nextInt(999999));

        usuario.setCodigoConfirmacao(codigo);
        usuario.setDataExpiracaoCodigo(LocalDateTime.now().plusMinutes(15)); // Expira em 15 minutos
        usuario.setStatus(StatusUsuario.PENDENTE_CONFIRMACAO);

        String subject = "Seu Código de Confirmação - Go Planner";
        String text = "Olá, " + usuario.getNome() + "!\n\n"
                + "Bem-vindo ao Go Planner. Use o código abaixo para ativar sua conta:\n\n"
                + "CÓDIGO: " + codigo + "\n\n"
                + "Este código expira em 15 minutos.";


        emailService.sendSimpleMessage(usuario.getEmail(), subject, text);

        return usuarioRepository.save(usuario);
    }

}