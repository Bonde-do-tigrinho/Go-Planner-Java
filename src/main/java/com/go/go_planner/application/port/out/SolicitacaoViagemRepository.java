package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.SolicitacaoAmizade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoViagemRepository extends MongoRepository<SolicitacaoAmizade, String> {
}