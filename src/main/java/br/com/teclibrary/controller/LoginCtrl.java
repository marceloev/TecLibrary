package br.com.teclibrary.controller;

import br.com.teclibrary.entity.User;
import br.com.teclibrary.service.RoleService;
import br.com.teclibrary.service.UserService;
import br.com.teclibrary.system.mail.DefaultMailMessages;
import br.com.teclibrary.system.task.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;

@Controller
public class LoginCtrl {

    @Autowired
    private UserService service;

    @GetMapping("/")
    private ModelAndView login(final Principal principal) {
        if (principal == null)
            return new ModelAndView("redirect:/login");
        else
            return new ModelAndView("redirect:/home");
    }

    @GetMapping("/login")
    private ModelAndView login_GET(final Principal principal,
                                   @RequestParam(value = "msg", required = false) String msg,
                                   @RequestParam(value = "error", required = false) String error) {
        if (principal != null) return new ModelAndView("redirect:/home");
        ModelAndView modelAndView = new ModelAndView("login");
        if (msg != null) modelAndView.getModelMap().addAttribute("msg", msg);
        if (error != null) modelAndView.getModelMap().addAttribute("error", "Usuário/Senha inválidos.");
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        ModelAndView modelAndView = new ModelAndView("redirect:/login");
        modelAndView.getModelMap().addAttribute("msg", "Logout efetuado com sucesso");
        return modelAndView;
    }

    @GetMapping("/cadastrar")
    private ModelAndView cadastrar_GET(User user) {
        ModelAndView modelAndView = new ModelAndView("cadastrar");
        return modelAndView;
    }

    @PostMapping("/cadastrar")
    private ModelAndView cadastrar_POST(@Valid @ModelAttribute("user") User user,
                                        BindingResult result) {
        try { //Verifica se já existe usuário com este login.
            Boolean exists = service.checkIfUserExists(user.getLogin());
            if (exists) result.addError(new ObjectError("AlreadyExists",
                    "Já existe um usuário cadastrado com este login."));
        } catch (Exception ex) {
            result.addError(new ObjectError("Exception", ex.getMessage()));
        }
        if (result.hasErrors()) return cadastrar_GET(user);
        user.setTelefone(user.getTelefone().replaceAll("[^0-9]", ""));
        user.setCpf(user.getCpf().replaceAll("[^0-9]", ""));
        user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
        user.setRole(RoleService.getRoleList().stream()
                .filter(genero -> genero.getDescricao().equals("USER"))
                .findFirst().get());
        try {
            service.insert(user);
        } catch (Exception ex) {
            result.addError(new ObjectError("error", ex.getMessage()));
            ex.printStackTrace();
            return cadastrar_GET(user);
        }
        sendAsyncWelcomeEmail(user);
        return login_GET(null, "Usuário cadastrado com sucesso!", null);
    }

    @GetMapping("/recuperar-senha")
    private ModelAndView recuperarSenha_GET(@RequestParam(value = "msg", required = false) String msg,
                                            @RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("recuperarSenha");
        if (msg != null) modelAndView.getModelMap().addAttribute("msg", msg);
        if (error != null) modelAndView.getModelMap().addAttribute("error", error);
        return modelAndView;
    }

    @PostMapping("/recuperar-senha")
    private ModelAndView recuperarSenha_POST(@RequestParam("login") String username,
                                             @RequestParam("email") String email) {
        ModelAndView modelAndView = new ModelAndView("redirect:/recuperar-senha");
        try {
            Boolean exists = service.checkIfUserExists(username);
            if (!exists)
                throw new Exception("Usuário não encontrado para o login digitado.");
            User user = service.findByLogin(username);
            if (!user.getEmail().toUpperCase().equals(email.toUpperCase()))
                throw new Exception("E-mail informado não é o mesmo cadastrado para o usuário.");
            String newPassword = String.format("%s%s",
                    String.valueOf(user.getCodigo()),
                    String.valueOf(new Date().getTime()));
            new DefaultMailMessages().sendRetrievePasswordMail(user, newPassword);
            user.setSenha(new BCryptPasswordEncoder().encode(newPassword));
            service.update(user);
            modelAndView.getModelMap().addAttribute("msg",
                    "E-mail para recuperação de senha enviado com sucesso.");
        } catch (Exception ex) {
            ex.printStackTrace();
            modelAndView.getModelMap().addAttribute("error", ex.getMessage());
        }
        return modelAndView;
    }

    private void sendAsyncWelcomeEmail(User user) {
        Runnable runSendWelcomeEmail = () -> {
            try {
                new DefaultMailMessages().sendWelcomeMail(user);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        new AsyncTask(runSendWelcomeEmail).trigger();
    }
}
