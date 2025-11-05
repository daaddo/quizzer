package it.cascella.quizzer.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.cascella.quizzer.dtos.Mail;
import it.cascella.quizzer.dtos.NewUserDTO;
import it.cascella.quizzer.entities.Role;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class MailService {

    private final  JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String FROM ;
    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;


    }

    public void sendEmail(String to, String subject, String text) throws MessagingException {

        log.info("Sending email to: " + to);
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setFrom(FROM);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(mail);

    }
    public void sendEmail(Mail mailToSend) throws QuizzerException {
        try {
            sendEmail(mailToSend.getTo(), mailToSend.getSubject(), mailToSend.getBody());
        }catch (Exception e){
            throw new QuizzerException("unknown exception", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }


}
