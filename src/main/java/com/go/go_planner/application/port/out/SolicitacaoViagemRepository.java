package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.SolicitacaoViagem;
import com.go.go_planner.domain.model.StatusSolicitacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoViagemRepository extends MongoRepository<SolicitacaoViagem, String> {
    boolean existsBySolicitadoIdAndIDviagemAndStatus(String solicitadoId, String idViagem, StatusSolicitacao status);
}