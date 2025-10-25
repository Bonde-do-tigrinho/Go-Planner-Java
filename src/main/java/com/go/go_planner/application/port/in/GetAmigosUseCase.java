package com.go.go_planner.application.port.in;

import com.go.go_planner.domain.model.Usuario;
import java.util.List;

public interface GetAmigosUseCase {
    List<Usuario> getAmigos(String userId);
}