package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.Viagem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ViagemRepository extends MongoRepository<Viagem, String> {
    List<Viagem> findByFavoritadaIsTrue();
}