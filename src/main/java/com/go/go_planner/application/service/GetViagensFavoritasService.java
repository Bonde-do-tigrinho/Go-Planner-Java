package com.go.go_planner.application.service;

import com.go.go_planner.application.port.in.GetViagensFavoritasUseCase;
import com.go.go_planner.application.port.out.UsuarioRepository;
import com.go.go_planner.application.port.out.ViagemRepository;
import com.go.go_planner.domain.model.Usuario;
import com.go.go_planner.domain.model.Viagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GetViagensFavoritasService implements GetViagensFavoritasUseCase {

    private final UsuarioRepository usuarioRepository;
    private final ViagemRepository viagemRepository;

    @Override
    public List<Viagem> execute(String userId) {
        // 1. Buscar o usuário para acessar a lista de IDs favoritos
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));

        List<String> idsFavoritos = usuario.getViagensFavoritasIds();

        // 2. Se a lista estiver vazia, retorna logo uma lista vazia (evita ir ao banco à toa)
        if (idsFavoritos == null || idsFavoritos.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. Busca todas as viagens cujos IDs estão na lista
        List<Viagem> viagensFavoritas = viagemRepository.findAllById(idsFavoritos);

        // 4. (Opcional) Ajustar o campo 'favoritada' para true no retorno
        // Isso ajuda o frontend a saber que elas já devem vir marcadas com o coraçãozinho preenchido
        viagensFavoritas.forEach(v -> v.setFavoritada(true));

        return viagensFavoritas;
    }
}
