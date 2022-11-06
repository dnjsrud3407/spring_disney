package com.tistory.dnjsrud.disney.global;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void send(String to, String title, String templateName, String password) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(title);

        // 템플릿에 전달할 비밀번호 설정
        Context context = new Context();
        context.setVariable("password", password);

        // 메일 내용 설정
        String html = templateEngine.process(templateName, context);
        helper.setText(html, true);

        javaMailSender.send(message);
    }
}
