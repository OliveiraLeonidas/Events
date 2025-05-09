package com.nlw_connect.events.service;

import com.nlw_connect.events.model.Events;
import com.nlw_connect.events.model.User;
import com.nlw_connect.events.utils.LocalDateFormatter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.temporal.ChronoUnit;

@Service
public class PDFService {
    @Autowired
    private TemplateEngine templateEngine;

    public ByteArrayOutputStream GeneratePDF(User subscriber, Events event) throws IOException {

        long eventTime = event.getStartTime().until(event.getEndTime(), ChronoUnit.HOURS);
        long days = event.getStartDate().until(event.getEndDate(), ChronoUnit.DAYS) + 1;



        Context context = new Context();
        context.setVariable("nome", subscriber.getName());
        context.setVariable("title", event.getTitle());
        context.setVariable("location", event.getLocation());
        context.setVariable("startDate", LocalDateFormatter.formatDate(event.getStartDate()));
        context.setVariable("endDate", LocalDateFormatter.formatDate(event.getEndDate()));
        context.setVariable("startTime", event.getStartTime());
        context.setVariable("endTime", event.getEndTime());
        context.setVariable("duracaoHoras", (days*eventTime));
        System.out.println(context.getVariableNames());

        String htmlContent = templateEngine.process("certificate", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(htmlContent, null);
        builder.toStream(outputStream);
        builder.run();
        return outputStream;
    }
}
