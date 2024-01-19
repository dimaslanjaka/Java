package com.dimaslanjaka.springusermgr.mailer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {
    private final Environment env;
    @Autowired
    private JavaMailSender mailSender;
    private String fromAddress;

    public EmailService(Environment env) {
        this.env = env;
        fromAddress = env.getProperty("spring.mail.username");
    }

    public void sendEmail(String to, String subject, String body) {
        fromAddress = env.getProperty("spring.mail.username");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendEmail(String to, String subject, String body, File attachment) {
        try {
            sendMailMultipart(to, subject, body, attachment);
        } catch (MessagingException e) {
            System.out.println("failed send email with attachment to " + to);
        }
    }

    public void sendMailMultipart(String toEmail, String subject, String message, File file) throws MessagingException {
        fromAddress = env.getProperty("spring.mail.username");
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message);

        if (file != null) {
            helper.addAttachment(file.getName(), file);
        }
        mailSender.send(mimeMessage);
    }

}