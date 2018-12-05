package br.com.teclibrary.system.mail;


import br.com.teclibrary.entity.Email;
import br.com.teclibrary.entity.User;
import br.com.teclibrary.system.impls.ModelPair;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class DefaultMailMessages {

    private final String remetente = "TecLibrary-App <teclibraryapp@gmail.com>";
    private Email email;

    public static final List<String> destinarioEmailBuilder(ModelPair... destinatarios) {
        List<String> destinatariosList = new ArrayList<>();
        for (ModelPair<String, String> destinatario : destinatarios)
            destinatariosList.add(destinatario.getKey().concat(" <").concat(destinatario.getValue()).concat(">"));
        return destinatariosList;
    }

    public void sendRetrievePasswordMail(User user, String codigo) throws Exception {
        final String assunto = "Recuperação de Senha - TecLibrary-App";
        this.setEmail(new Email(remetente,
                destinarioEmailBuilder(new ModelPair(user.getNome(), user.getEmail())),
                assunto,
                getPasswordRetrieveBodyHTML(user, codigo)));
        send();
    }

    public void sendWelcomeMail(User user) throws Exception {
        final String assunto = "Seja Bem Vindo ao nosso time, -TecLibrary-App";
        this.setEmail(new Email(remetente,
                destinarioEmailBuilder(new ModelPair(user.getNome(), user.getEmail())),
                assunto,
                getWelcomeMailBodyHTML(user)));
        send();
    }

    public final String getWelcomeMailBodyHTML(User user) {
        return "\t\t\t<html><head><meta charset=\"utf-8\"></head><p>Olá, <strong>" + user.getNome() + "</strong>!</p>\n" +
                "\t\t\t<br>\n" +
                "\t\t\t<p>Seja bem vindo ao time TecLibrary-App, ficamos muito feliz com a sua chegada.</p>\n" +
                "\t\t\t<p>Você agora tem acesso para download de todos os nossos livros, fique a vontade para usá-los.</p>\n" +
                "\t\t\t<p>Em caso de dúvidas, sugestões, feedback ou qualquer contato que queira ter com a gente,</p>\n" +
                "\t\t\t<p>estamos disponíveis através do e-mail <a href=\"mailto:teclibraryapp@gmail.com\">teclibraryapp@gmail.com</a>.</p>\n" +
                "\t\t\t<br>\n" +
                "\t\t\t<p>Desde já, obrigado.</p>\n" +
                "\t\t\t<p>Atenciosamente,</p>\n" +
                "\t\t\t<p>Equipe de suporte da TecLibrary-App.</p>\n" +
                "\t\t\t<p><img src=\"http://blog.maxieduca.com.br/wp-content/uploads/2016/05/iStock_000083601441_Large-1024x728.jpg\" alt=\"Resultado de imagem para biblioteca min image\" width=\"290\" height=\"150\" /></p></html>";
    }

    public final String getPasswordRetrieveBodyHTML(User user, String codigo) {
        return "<html><p>Olá, <strong>".concat(user.getNome()).concat("</strong>!</p>").concat(
                "<p>Nós, da equipe TecLibrary-App, recebemos em nosso sistema, uma solicitação de recuperação de senha para o seu usuário, exatamente neste momento.<br />Caso não tenha sido voc&ecirc; quem solicitou, ou tenha solicitado por engano, por favor ignore este e-mail.<br />Caso tenha sido solicitado propositalmente, segue abaixo a nova senha que poder&aacute; ser utilizada:</p>").concat(
                "<p>").concat(codigo).concat("</p>").concat(
                "<p>Atenciosamente,</p>").concat(
                "<p>Equipe de suporte do sistema TecLibrary-App.</p>").concat(
                "<p>Acesse-nos em: <a href=\"https://teclibrary-app.heroku.com\">TecLibrary-App</a></p>").concat(
                "<p><img src=\"http://blog.maxieduca.com.br/wp-content/uploads/2016/05/iStock_000083601441_Large-1024x728.jpg\" alt=\"Resultado de imagem para biblioteca min image\" width=\"290\" height=\"150\" /></p></html>");
    }

    public void send() throws Exception {
        if (getEmail() == null) throw new Exception("E-mail p/ envio não configurado");
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                DefaultMailMessages.class.getPackage().getName());
        CustomMailSender customMailSender = applicationContext.getBean(CustomMailSender.class);
        customMailSender.enviar(this.getEmail());
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
}
