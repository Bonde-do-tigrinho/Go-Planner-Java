package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Atividade;
import java.util.List;

public interface GetAtividadesUseCase {
    List<Atividade> getAtividades(String viagemId);
}