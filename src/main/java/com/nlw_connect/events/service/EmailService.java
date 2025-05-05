package com.nlw_connect.events.service;

import com.nlw_connect.events.model.Event;
import com.nlw_connect.events.model.User;
import com.nlw_connect.events.repository.EventRepo;
import com.nlw_connect.events.repository.UserRepo;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private String from;
    private String to;
    private String TEMPLATE_HTML;
    private String subject;
    private String text;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PDFService pdfService;

    // TODO: Fazer inversão de dependências
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

        // TODO: refatorar para utilizar uma instância de User e não um HashMap
        Map<String, Object> data = new HashMap<>();
        data.put("nome", subscriber.getName());
        data.put("evento", event.getTitle());
        data.put("data", event.getStartDate());
        data.put("hora", event.getStartTime());
        data.put("local", event.getLocation());
        data.put("url", "https://thisevent.com/evento/" + event.getPrettyName());

        //System.out.println(data.get("nome"));

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

//            mailSender.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException("Error where trying send confirmation email", e);
        }

    }

    // TODO: Desacoplar envio do e-mail da geração do certificado
    public ByteArrayOutputStream sendEventCertificate(int userId, int eventId) throws IOException, MessagingException {
        User subscriber = userRepo.findUserById(userId);
        Event event = eventRepo.findByEventId(eventId);

        ByteArrayOutputStream outputStream = pdfService.GeneratePDF(subscriber, event);
        String textMessage = "Olá " + subscriber.getName() + ", segue em anexo o certificado de participação no evento \"" + event.getTitle() + "\".";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("leonidasoliv25@gmail.com");
        helper.setTo(subscriber.getEmail());
        helper.setSubject("Certificado de Participação: " + event.getTitle());
        helper.addAttachment("certificate.pdf", new ByteArrayResource(outputStream.toByteArray()));
        helper.setText(textMessage, false);

        // mailSender.send(message);

        return outputStream;
    }
}
