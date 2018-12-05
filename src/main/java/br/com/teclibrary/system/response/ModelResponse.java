package br.com.teclibrary.system.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelResponse {

    public enum ResponseType { None, Error, Success, Warn, Loading }
    private String mensagem = "";
    private Throwable throwable;
    private ResponseType responseType = ResponseType.None;

    public ModelResponse() {

    }

    public ModelResponse(String mensagem, Throwable throwable, ResponseType responseType) {
        this.mensagem = mensagem;
        this.throwable = throwable;
        this.responseType = responseType;
    }
}
