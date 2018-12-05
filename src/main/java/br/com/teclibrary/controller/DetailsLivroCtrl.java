package br.com.teclibrary.controller;

import br.com.teclibrary.entity.Livro;
import br.com.teclibrary.entity.User;
import br.com.teclibrary.service.LivroService;
import br.com.teclibrary.system.db.ConnectionFactory;
import br.com.teclibrary.system.db.ModelConnection;
import br.com.teclibrary.system.db.QueryType;
import br.com.teclibrary.system.impls.ModelPair;
import br.com.teclibrary.system.response.ModelResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DetailsLivroCtrl {

    Logger logger = LoggerFactory.getLogger(DetailsLivroCtrl.class);

    @Autowired
    private LivroService livroService;

    @GetMapping("/detalhes-livro/{ID}")
    public ModelAndView detalhesLivroID_GET(@PathVariable("ID") String ID) {
        try { //Necessário, pois o spring aproveita a conexão.
            ModelConnection modelConnection = ConnectionFactory.requestNewConnection(20);
            Livro livro = livroService.findByPK(Integer.valueOf(ID), modelConnection);
            if (livro == null) throw new Exception("Livro de código ".concat(ID).concat(" não encontrado"));
            ModelAndView modelAndView = new ModelAndView("detalhesLivro");
            modelAndView.getModelMap().addAttribute("livro", livro);
            List<Livro> livroListRelacionados = this.livroService.findBookLookALike(livro, modelConnection);
            modelAndView.getModelMap().addAttribute("booksRelacionados", livroListRelacionados);
            return modelAndView;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ModelAndView modelAndView = new ModelAndView("redirect:/home");
            modelAndView.getModelMap().addAttribute("error", ex.getMessage());
            return modelAndView;
        }
    }

    @PostMapping(value = "/send-problem", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModelResponse> sendReportProblem(@RequestBody String JSON,
                                                           @AuthenticationPrincipal User user,
                                                           Errors errors) {
        JSONObject json = new JSONObject(JSON);
        String resumo = json.getString("resumo");
        String problema = json.getString("problema");
        if (resumo == null || resumo.isEmpty()) {
            return ResponseEntity.badRequest().body(ModelResponse.builder()
                    .mensagem("O Resumo do Feedback não pode ser vazio.")
                    .responseType(ModelResponse.ResponseType.Error)
                    .build());
        } else if (problema == null || problema.isEmpty()) {
            return ResponseEntity.badRequest().body(ModelResponse.builder()
                    .mensagem("O Problema do Feedback não pode ser vazio.")
                    .responseType(ModelResponse.ResponseType.Error)
                    .build());
        }
        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(ModelResponse.builder()
                    .mensagem(errors.getAllErrors()
                            .stream().map(erro -> erro.getDefaultMessage())
                            .collect(Collectors.joining(",")))
                    .responseType(ModelResponse.ResponseType.Error)
                    .build());
        try {
            livroService.executeStatement("INSERT INTO TFEED (CODFEED, OBSFEED, CODLIVRO, CODUSU, RESUMOFEED) " +
                            "VALUES (nextval ('SEQ_TFEED'), :P_FEED, :P_CODLIVRO, :P_CODUSU, :P_RESUMO)", QueryType.Native_Query,
                    new ModelPair("P_FEED", problema),
                    new ModelPair("P_CODLIVRO", json.getInt("id")),
                    new ModelPair("P_CODUSU", user.getCodigo()),
                    new ModelPair("P_RESUMO", resumo));
            return ResponseEntity.ok(ModelResponse.builder()
                    .mensagem("Feedback registrado com sucesso.")
                    .responseType(ModelResponse.ResponseType.Success)
                    .build());
        } catch (Exception ex) {
            String msgError = "Erro ao tentar registrar Feedback: " + ex.getMessage();
            logger.error(msgError, ex);
            return ResponseEntity.badRequest().body(ModelResponse.builder()
                    .mensagem(msgError)
                    .responseType(ModelResponse.ResponseType.Error)
                    .build());
        }
    }

}