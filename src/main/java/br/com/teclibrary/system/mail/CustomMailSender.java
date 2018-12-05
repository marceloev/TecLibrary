package br.com.teclibrary.system.mail;

import br.com.teclibrary.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Component
public class CustomMailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviar(Email email) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject(email.getAssunto());

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(email.getRemetente());
        helper.setTo(email.getDestinatarios().toArray(new String[email.getDestinatarios().size()]));

        BodyPart body = new MimeBodyPart();
        body.setContent(email.getCorpo(), "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);

        message.setContent(multipart);

        javaMailSender.send(message);

    }
}
