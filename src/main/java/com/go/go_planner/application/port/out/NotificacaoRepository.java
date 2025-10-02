package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.Notificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends MongoRepository<Notificacao, String> {

    // Encontra todas as notificações para um usuário, ordenadas pela mais recente
    List<Notificacao> findByDestinatarioIdOrderByDataCriacaoDesc(String destinatarioId);

    // Conta quantas notificações não lidas um usuário tem (útil para o "sininho")
    long countByDestinatarioIdAndLidaIsFalse(String destinatarioId);
}