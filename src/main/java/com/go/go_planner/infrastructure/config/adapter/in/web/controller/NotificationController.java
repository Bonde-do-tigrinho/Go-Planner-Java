package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepository;
import com.go.go_planner.domain.model.Notificacao;
import com.go.go_planner.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificacaoRepository notificacaoRepository;

    @GetMapping("/minhasNotificacoes") // Uma rota mais segura como "/minhas" ou "/me"
    public ResponseEntity<List<Notificacao>> getMinhasNotificacoes(
            @AuthenticationPrincipal Usuario usuarioLogado // Injeta o utilizador autenticado
    ) {
        // Usa o ID do utilizador logado (vindo do token) para a busca
        var notifications = notificacaoRepository.findByDestinatarioIdOrderByDataCriacaoDesc(usuarioLogado.getId());

        return ResponseEntity.ok(notifications);
    }
}
