package com.ekalavya.org.service;

import com.ekalavya.org.entity.User;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
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
//        ClassPathResource image = new ClassPathResource("/static/images/logo.png"); // Adjust the path as needed
//        helper.addInline("logoImage", image);

        try {
            mailSender.send(message);
            logger.info("OTP email sent successfully: {}", otp);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to send OTP email: " + e.getMessage());
        }
    }

    @Async
    public void sendApprovalEmail(User user) throws MessagingException, IOException, TemplateException {
        MimeMessage message = mailSender.createMimeMessage();
        Map<String, Object> model = new HashMap<>();
        model.put("userName", user.getUsername());
        model.put("employeeId", user.getEmpId());
        model.put("roleName", user.getRole().getName());
        model.put("loginUrl", "http://localhost:8080/login");
        model.put("portalName", "Ekalavya Foundation");
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        Template t = freemarkerConfig.getTemplate("account-approval-email.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

        helper.setTo(user.getEmailid());
        helper.setText(html, true);
        helper.setSubject("Welcome to Ekalavya – Your Account is Ready!");
        helper.setFrom(from);

        mailSender.send(message);
    }
}
