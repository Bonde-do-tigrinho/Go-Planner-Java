package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetMinhasNotificacoesUseCase;
import com.go.go_planner.application.port.out.NotificacaoRepository;
import com.go.go_planner.domain.model.Notificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMinhasNotificacoesService implements GetMinhasNotificacoesUseCase {

    private final NotificacaoRepository notificacaoRepository;

    @Override
    public List<Notificacao> execute(String userId) {
        return notificacaoRepository.findByDestinatarioIdAndLidaFalseOrderByDataCriacaoDesc(userId);
    }

}
