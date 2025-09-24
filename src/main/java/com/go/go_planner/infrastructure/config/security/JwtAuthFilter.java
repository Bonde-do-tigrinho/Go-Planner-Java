package com.go.go_planner.infrastructure.config.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // 1. Torna a classe um Bean do Spring, para que possamos injetá-la
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String firebaseIdToken = authHeader.substring(7);

        try {
            // Verifica o token usando o Firebase Admin SDK
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String userEmail = decodedToken.getEmail();

            // Se o token for válido e o usuário ainda não estiver autenticado no contexto do Spring
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Busca os detalhes do usuário no seu banco de dados (Firestore)
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Cria um objeto de autenticação e o coloca no contexto de segurança
                // A partir deste ponto, o Spring considera o usuário como "logado" para esta requisição
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Ocorreu um erro na validação do token (inválido, expirado, etc.)
            System.err.println("Falha na autenticação do token do Firebase: " + e.getMessage());
        }

        // Continua o fluxo da requisição
        filterChain.doFilter(request, response);
    }
}