package com.go.go_planner.application.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;


    @Async
    public void sendSimpleMessage(String destinatario, String assunto, String texto) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("goplanner31@gmail.com");
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(texto);
            emailSender.send(message);
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

}
