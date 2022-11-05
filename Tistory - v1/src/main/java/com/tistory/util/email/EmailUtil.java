package com.tistory.util.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailUtil {
	
	private final JavaMailSender sender;

    public void sendEmail(String toAddress, String subject, String body) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper mimeMessagehelper = new MimeMessageHelper(message);
        try {
        	mimeMessagehelper.setTo(toAddress);
        	mimeMessagehelper.setSubject(subject);
        	mimeMessagehelper.setText(body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        sender.send(message);

    }
}
