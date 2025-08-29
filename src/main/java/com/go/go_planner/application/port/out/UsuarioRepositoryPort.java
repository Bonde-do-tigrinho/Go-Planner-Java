package com.go.go_planner.application.port.out;

import com.go.go_planner.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(String id); // Alterado para String, pois Firestore usa IDs alfanuméricos
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
