package br.com.teclibrary.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Email {

    private String remetente;
    private List<String> destinatarios;
    private String assunto;
    private String corpo;

    public Email(String remetente, List<String> destinatarios, String assunto, String corpo) {
        this.remetente = remetente;
        this.destinatarios = destinatarios;
        this.assunto = assunto;
        this.corpo = corpo;
    }
}
