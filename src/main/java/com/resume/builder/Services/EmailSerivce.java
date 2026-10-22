package com.resume.builder.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSerivce {

    @Value("${spring.mail.properties.mail.smtp.from}")
    private final String fromEmail;

    private final JavaMailSender mailSender;

    public EmailSerivce(
            @Value("${spring.mail.properties.mail.smtp.from}")
            String fromEmail,
            JavaMailSender mailSender) {

        this.fromEmail = fromEmail;
        this.mailSender = mailSender;
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        log.info("Inside Email Services - sendHtmlEmail(): {}, {}, {}", to, subject, htmlContent );
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String to,
                                               String subject,
                                               String body,
                                               byte[] attachment,
                                               String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        helper.addAttachment(fileName, new ByteArrayResource(attachment));
        mailSender.send(message);
    }

}
