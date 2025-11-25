package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.Viagem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ViagemRepository extends MongoRepository<Viagem, String> {
    List<Viagem> findByFavoritadaIsTrue();
    List<Viagem> findByCriadorViagemID(String criadorId);
    List<Viagem> findAllById(Iterable<String> ids);
    List<Viagem> findByParticipantesUserId(String userId);
}