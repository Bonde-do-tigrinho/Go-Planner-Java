package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.SolicitacaoAmizade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository; // <-- GARANTA QUE ESTE IMPORT E A ANOTAÇÃO EXISTEM

import java.util.Optional;

@Repository // <-- ESTA ANOTAÇÃO É ESSENCIAL
public interface SolicitacaoAmizadeRepository extends MongoRepository<SolicitacaoAmizade, String> {

    @Query("{ $and: [ { 'status': 'PENDENTE' }, { $or: [ { 'solicitanteId': ?0, 'solicitadoId': ?1 }, { 'solicitanteId': ?1, 'solicitadoId': ?0 } ] } ] }")
    Optional<SolicitacaoAmizade> findPendenteByParticipantes(String idUsuario1, String idUsuario2);

}