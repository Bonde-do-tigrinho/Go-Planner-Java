package com.go.go_planner.infrastructure.config.adapter.out.persistence;

import com.go.go_planner.application.port.out.UsuarioRepositoryPort;
import com.go.go_planner.domain.model.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private static final String COLLECTION_NAME = "usuarios";
    private final Firestore firestore;

    @Override
    public Usuario save(Usuario usuario) {
        try {
            CollectionReference usuariosCollection = firestore.collection(COLLECTION_NAME);

            if (usuario.getId() == null || usuario.getId().isBlank()) {
                // Criar novo usuário (ID gerado pelo Firestore)
                DocumentReference docRef = usuariosCollection.document();
                usuario.setId(docRef.getId()); // Atribui o novo ID ao objeto
                docRef.set(usuario).get(); // .get() espera a operação ser concluída
            } else {
                // Atualizar usuário existente
                usuariosCollection.document(usuario.getId()).set(usuario, SetOptions.merge()).get();
            }
            return usuario;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao salvar usuário no Firestore: {}", e.getMessage());
            throw new RuntimeException("Erro de comunicação com o banco de dados.", e);
        }
    }

    @Override
    public Optional<Usuario> findById(String id) {
        try {
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return Optional.ofNullable(document.toObject(Usuario.class));
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao buscar usuário por ID no Firestore: {}", e.getMessage());
            throw new RuntimeException("Erro de comunicação com o banco de dados.", e);
        }
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        try {
            CollectionReference usuarios = firestore.collection(COLLECTION_NAME);
            Query query = usuarios.whereEqualTo("email", email).limit(1);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            if (!querySnapshot.get().isEmpty()) {
                DocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
                return Optional.ofNullable(document.toObject(Usuario.class));
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao buscar usuário por email no Firestore: {}", e.getMessage());
            throw new RuntimeException("Erro de comunicação com o banco de dados.", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            CollectionReference usuarios = firestore.collection(COLLECTION_NAME);
            Query query = usuarios.whereEqualTo("email", email).limit(1);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            return !querySnapshot.get().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao verificar existência de email no Firestore: {}", e.getMessage());
            throw new RuntimeException("Erro de comunicação com o banco de dados.", e);
        }
    }
}