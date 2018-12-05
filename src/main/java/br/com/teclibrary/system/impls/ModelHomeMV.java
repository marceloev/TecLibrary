package br.com.teclibrary.system.impls;

import br.com.teclibrary.entity.Genero;
import br.com.teclibrary.service.GeneroService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.stream.Collectors;

public class ModelHomeMV extends ModelAndView {

    private GeneroService service = new GeneroService();

    public ModelHomeMV() {
        this.setViewName("home");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.getModelMap().addAttribute("name", auth.getName().toUpperCase());
        this.getModelMap().addAttribute("generosList",
                service.findAll().stream().sorted(Comparator.comparing(Genero::getDescricao))
                        .collect(Collectors.toList()));
        auth.getAuthorities().forEach(role ->
                this.getModelMap().addAttribute("ROLE_" + role, true));
    }
}
