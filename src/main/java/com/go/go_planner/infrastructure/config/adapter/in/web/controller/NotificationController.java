package com.go.go_planner.infrastructure.config.adapter.in.web.controller;

import com.go.go_planner.application.port.in.GetMinhasNotificacoesUseCase;
import com.go.go_planner.application.port.in.GetNotificacaoByIdUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
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

    private final GetMinhasNotificacoesUseCase getMinhasNotificacoesUseCase;
    private final NotificacaoRepository notificacaoRepository;
    private final GetNotificacaoByIdUseCase getNotificacaoByIdUseCase; // <--- INJETAR

    @GetMapping("/minhas-notificacoes")
    public ResponseEntity<List<Notificacao>> getMinhasNotificacoesNaoLidas(
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        List<Notificacao> notificacoes = getMinhasNotificacoesUseCase.execute(usuarioLogado.getId());

        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacao> getNotificacaoStatus(
            @PathVariable String id,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        Notificacao notificacao = getNotificacaoByIdUseCase.execute(id, usuarioLogado.getId());

        return ResponseEntity.ok(notificacao);
    }
}
