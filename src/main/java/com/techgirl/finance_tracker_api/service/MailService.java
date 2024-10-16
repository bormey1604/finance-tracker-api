package com.techgirl.finance_tracker_api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.IOException;
import java.util.Objects;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender mailSender;

    public String sendEmailWithAttachment(String receiver,
                                          String subject,
                                          String messageBody,
                                          String type,
                                          byte[] file) throws MessagingException, IOException {


        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);


        helper.setFrom(sender, "Finance Tracker");
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(messageBody);


        if (file != null && file.length > 0) {
            String attachmentFileName = type+"_transactions.pdf";
            DataSource byteArrayDataSource = new ByteArrayDataSource(file, "application/pdf");
            helper.addAttachment(attachmentFileName, byteArrayDataSource);
        } else {
            throw new IllegalArgumentException("File must not be null or empty");
        }


        mailSender.send(mimeMessage);

        return "Email with attachment sent successfully!";
    }

}
