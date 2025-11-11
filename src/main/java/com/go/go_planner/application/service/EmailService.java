package com.go.go_planner.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sendinblue.ApiException;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final TransactionalEmailsApi brevoApi;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    @Async // Continuamos a enviar de forma assíncrona para não bloquear a API
    public void sendSimpleMessage(String to, String subject, String textContent) {
        try {
            // 1. Define o remetente (precisa de ser um e-mail verificado no Brevo)
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(senderEmail);
            sender.setName(senderName);

            // 2. Define o destinatário
            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(to);

            // 3. Monta o e-mail
            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(sender);
            email.setTo(List.of(recipient));
            email.setSubject(subject);
            email.setTextContent(textContent);

            // 4. Envia o e-mail através da API do Brevo
            CreateSmtpEmail result = brevoApi.sendTransacEmail(email);

            System.out.println("E-mail enviado com sucesso via API Brevo. Message ID: " + result.getMessageId());

        } catch (ApiException e) {
            System.err.println("Erro da API do Brevo!");
            System.err.println("Código de Status HTTP: " + e.getCode());
            System.err.println("Corpo da Resposta (Erro): " + e.getResponseBody());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro geral ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }
}