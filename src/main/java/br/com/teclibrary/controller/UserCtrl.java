package br.com.teclibrary.controller;

import br.com.teclibrary.DAO.RoleDAO;
import br.com.teclibrary.entity.User;
import br.com.teclibrary.service.UserService;
import br.com.teclibrary.system.response.ModelResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
public class UserCtrl {

    private static final Logger logger = LoggerFactory.getLogger(UserCtrl.class);

    @Autowired
    private UserService service;

    @GetMapping("/cadastro/usuarios")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroUsuarios_GET(User user,
                                             @RequestParam(value = "user", required = false) User userAttribute) {
        ModelAndView modelAndView = new ModelAndView("cadastrarUsuario");
        if (userAttribute == null || userAttribute.getCodigo() == 0)
            modelAndView.getModelMap().addAttribute("user", user);
        else
            modelAndView.getModelMap().addAttribute("user", userAttribute);
        modelAndView.getModelMap().addAttribute("roleList", RoleDAO.getRoleList());
        modelAndView.getModelMap().addAttribute("userList", service.findAll());
        return modelAndView;
    }

    @GetMapping("/cadastro/usuarios/new")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String cadastroUsuariosNEW_GET() {
        return "redirect:/cadastro/usuarios";
    }

    @GetMapping("/cadastro/usuarios/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroUsuariosID_GET(@PathVariable("id") String id,
                                               RedirectAttributes redirectAttributes) {
        try {
            User user = service.findByPK(Integer.valueOf(id));
            ModelAndView modelAndView = new ModelAndView("redirect:/cadastro/usuarios");
            redirectAttributes.addFlashAttribute("user", user);
            return modelAndView;
        } catch (Exception ex) {
            ex.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("redirect:/cadastro/usuarios");
            modelAndView.getModelMap().addAttribute("error", ex.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/delete/usuarios/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView deleteUsuariosID_GET(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/cadastro/usuarios");
        try {
            service.delete(Integer.valueOf(id));
        } catch (Exception ex) {
            modelAndView.getModelMap().addAttribute("error", ex.getMessage());
        }
        return modelAndView;
    }

    @PostMapping("/cadastro/usuarios")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroUsuarios_POST(@Valid @ModelAttribute("user") User user,
                                              BindingResult result,
                                              @RequestParam("password2") String senha2) {
        try {
            final boolean inserting = (user.getCodigo() == 0);
            if (inserting && service.checkIfUserExists(user.getLogin()))
                throw new Exception("Já existe um usuário cadastrado para este login");
            user.setTelefone(user.getTelefone().replaceAll("[^0-9]", ""));
            user.setCpf(user.getCpf().replaceAll("[^0-9]", ""));
            if (user.getSenha() == null || user.getSenha().isEmpty())
                user.setSenha(senha2);
            else
                user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
            if (inserting) {
                user.setDhCadastro(new Date());
                user.setDhUltAlteracao(new Date());
                service.insert(user);
            } else {
                service.update(user);
            }
            ModelAndView modelAndView = cadastroUsuarios_GET(user, null);
            modelAndView.getModelMap().addAttribute("msg",
                    String.format("Usuário %s - %s %s com sucesso.",
                            String.valueOf(user.getCodigo()),
                            user.getLogin(),
                            (inserting ? "cadastrado" : "editado")));
            return modelAndView;
        } catch (Exception ex) {
            ex.printStackTrace();
            result.addError(new ObjectError("error", ex.getMessage()));
            return cadastroUsuarios_GET(user, null);
        }
    }

    @PostMapping(value = "/changePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModelResponse> sendReportProblem(@RequestBody String JSON,
                                                           @AuthenticationPrincipal User user,
                                                           Errors errors) {
        JSONObject json = new JSONObject(JSON);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String unencoded = json.getString("oldSenha");
        String novaSenha = encoder.encode(json.getString("newSenha"));
        if (!encoder.matches(unencoded, user.getPassword())) {
            return ResponseEntity.badRequest().body(ModelResponse.builder()
                    .mensagem("A senha informada é divergente da senha atual do usuário.")
                    .responseType(ModelResponse.ResponseType.Error)
                    .build());
        }
        try {
            if (errors.hasErrors()) throw new Exception(errors.getAllErrors()
                    .stream().map(erro -> erro.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            user.setSenha(novaSenha);
            service.update(user);
            return ResponseEntity.ok(ModelResponse.builder()
                    .responseType(ModelResponse.ResponseType.Success)
                    .mensagem("A senha do usuário foi alterada com sucesso.")
                    .build());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ModelResponse.builder()
                    .responseType(ModelResponse.ResponseType.Error)
                    .mensagem("Erro ao tentar alterar a senha do usuário\n" + ex.getMessage())
                    .build());
        }
    }
}
