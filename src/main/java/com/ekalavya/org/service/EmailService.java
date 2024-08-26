package com.ekalavya.org.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendOtpEmail(String to, String otp) throws MessagingException, IOException, TemplateException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your Verification Code");
        helper.setFrom(from);

        Map<String, Object> model = new HashMap<>();
        model.put("code", otp);

        Template template = freemarkerConfig.getTemplate("verification-code.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        helper.setText(html, true);

        // Attach the image inline
        ClassPathResource image = new ClassPathResource("/static/images/logo.png"); // Adjust the path as needed
        helper.addInline("logoImage", image);

        try {
            mailSender.send(message);
            logger.info("OTP email sent successfully: {}", otp);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to send OTP email: " + e.getMessage());
        }
    }
}
