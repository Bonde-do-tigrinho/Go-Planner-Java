package com.go.go_planner.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class EmailService {

    private final TransactionalEmailsApi brevoApi;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    @Async
    public void sendSimpleMessage(String to, String subject, String textContent) {
        try {
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(senderEmail);
            sender.setName(senderName);

            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(to);

            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(sender);
            email.setTo(List.of(recipient));
            email.setSubject(subject);
            email.setTextContent(textContent);

            CreateSmtpEmail result = brevoApi.sendTransacEmail(email);

            log.error("E-mail enviado com sucesso via API Brevo. Message ID: " + result.getMessageId());

        } catch (ApiException e) {
            log.error("Erro da API do Brevo!");
            log.error("Código de Status HTTP: " + e.getCode());
            log.error("Corpo da Resposta (Erro): " + e.getResponseBody());
        } catch (Exception e) {
            log.error("Ocorreu um erro geral ao enviar e-mail: " + e.getMessage());
        }
    }
}