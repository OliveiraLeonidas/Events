package com.nlw_connect.events.service;

import com.nlw_connect.events.model.Event;
import com.nlw_connect.events.model.Subscription;
import com.nlw_connect.events.model.User;
import com.nlw_connect.events.repository.EventRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EventRepo eventRepo;

//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

    @Async
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("leonidasoliv25@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Async
    public void sendConfirmationSubscription(User subscriber, int eventId) {
        Event event = eventRepo.findByEventId(eventId);

        Map<String, Object> data = new HashMap<>();
        data.put("nome", subscriber.getName());
        data.put("evento", event.getTitle());
        data.put("data", event.getStartDate());
        data.put("hora", event.getStartTime());
        data.put("local", event.getLocation());
        data.put("url", "https://thisevent.com/evento/" + event.getPrettyName());

        Context context = new Context();
        context.setVariables(data);

        String bodyHtml = templateEngine.process("confirmation-subs", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("leonidasoliv25@gmail.com");
            helper.setTo(subscriber.getEmail());
            helper.setSubject("Inscrição Confirmada: " + event.getTitle());
            helper.setText(bodyHtml, true);

            mailSender.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException("Error where trying send confirmation email", e);
        }

    }
}
