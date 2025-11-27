package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.Notificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends MongoRepository<Notificacao, String> {

    List<Notificacao> findByDestinatarioIdOrderByDataCriacaoDesc(String destinatarioId);
    List<Notificacao> findByReferenciaId(String referenciaId);
    List<Notificacao> findByDestinatarioIdAndLidaFalseOrderByDataCriacaoDesc(String destinatarioId);
}