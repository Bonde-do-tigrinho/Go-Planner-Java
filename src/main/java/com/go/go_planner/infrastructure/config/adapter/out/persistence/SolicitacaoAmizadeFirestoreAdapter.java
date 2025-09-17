package com.go.go_planner.infrastructure.config.adapter.out.persistence;

import com.go.go_planner.application.port.out.SolicitacaoAmizadeRepositoryPort;
import com.go.go_planner.domain.model.SolicitacaoAmizade;
import com.go.go_planner.domain.model.StatusSolicitacao;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
@Component
@RequiredArgsConstructor
public class SolicitacaoAmizadeFirestoreAdapter implements SolicitacaoAmizadeRepositoryPort {

    private static final String COLLECTION_NAME = "solicitacoes_amizade";
    private final Firestore firestore;

    @Override
    public SolicitacaoAmizade save(SolicitacaoAmizade solicitacao) {
        var documentReference = firestore.collection(COLLECTION_NAME).document();
        solicitacao.setId(documentReference.getId()); // Pega o ID gerado e seta no objeto
        documentReference.set(solicitacao);
        return solicitacao;
    }

    @Override
    public void update(SolicitacaoAmizade solicitacao) {
        if (solicitacao.getId() == null || solicitacao.getId().isBlank()) {
            throw new IllegalArgumentException("ID da solicitação não pode ser nulo para atualização.");
        }
        firestore.collection(COLLECTION_NAME)
                .document(solicitacao.getId())
                .set(solicitacao); // .set() sobrescreve o documento inteiro
    }

    @Override
    public Optional<SolicitacaoAmizade> findPendenteByParticipantes(String idUsuario1, String idUsuario2) {
        // Firestore não suporta queries OR complexas nativamente.
        // A abordagem mais simples é fazer duas queries e unir os resultados.

        // Query 1: usuario1 enviou para usuario2
        Query query1 = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("solicitanteId", idUsuario1)
                .whereEqualTo("solicitadoId", idUsuario2)
                .whereEqualTo("status", StatusSolicitacao.PENDENTE);

        // Query 2: usuario2 enviou para usuario1
        Query query2 = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("solicitanteId", idUsuario2)
                .whereEqualTo("solicitadoId", idUsuario1)
                .whereEqualTo("status", StatusSolicitacao.PENDENTE);

        try {
            ApiFuture<QuerySnapshot> future1 = query1.get();
            ApiFuture<QuerySnapshot> future2 = query2.get();

            List<SolicitacaoAmizade> results1 = future1.get().toObjects(SolicitacaoAmizade.class);
            if (!results1.isEmpty()) {
                return Optional.of(results1.get(0));
            }

            List<SolicitacaoAmizade> results2 = future2.get().toObjects(SolicitacaoAmizade.class);
            if (!results2.isEmpty()) {
                return Optional.of(results2.get(0));
            }

        } catch (InterruptedException | ExecutionException e) {
            // Lide com a exceção de forma apropriada (ex: log, lançar uma exceção customizada)
            Thread.currentThread().interrupt();
            throw new RuntimeException("Erro ao buscar solicitação de amizade no Firestore", e);
        }

        return Optional.empty(); // Nenhuma solicitação encontrada
    }
}
