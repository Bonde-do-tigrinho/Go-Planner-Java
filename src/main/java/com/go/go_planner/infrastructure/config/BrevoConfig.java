package com.go.go_planner.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sendinblue.ApiClient;
import sibApi.TransactionalEmailsApi;

@Configuration
public class BrevoConfig {

    // Lê a chave de API do seu ficheiro .env ou das variáveis de ambiente do Render
    @Value("${brevo.api.key}")
    private String apiKey;

    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setApiKey(apiKey);
        return apiClient;
    }

    @Bean
    public TransactionalEmailsApi transactionalEmailsApi(ApiClient apiClient) {
        return new TransactionalEmailsApi(apiClient);
    }
}