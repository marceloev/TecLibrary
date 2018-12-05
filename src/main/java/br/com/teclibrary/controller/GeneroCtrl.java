package br.com.teclibrary.controller;

import br.com.teclibrary.entity.Genero;
import br.com.teclibrary.service.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class GeneroCtrl {

    @Autowired
    private GeneroService service;

    @GetMapping("/cadastro/generos")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroGeneros_GET(Genero genero,
                                            @RequestParam(value = "error", required = false) String error,
                                            @RequestParam(value = "msg", required = false) String msg) {
        ModelAndView modelAndView = new ModelAndView("cadastrarGenero");
        if (error != null) modelAndView.getModelMap().addAttribute("error", error);
        if (msg != null) modelAndView.getModelMap().addAttribute("msg", msg);
        modelAndView.getModelMap().addAttribute("generosList", service.findAll());
        return modelAndView;
    }

    @GetMapping("/delete/generos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView deleteGeneros_GET(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/cadastro/generos");
        try {
            Optional<Genero> generoOptional = service.findAll().stream()
                    .filter(genero -> genero.getCodigo() == Integer.valueOf(id)).findFirst();
            if (generoOptional.isPresent()) {
                if (service.isGeneroUsed(Integer.valueOf(id)))
                    throw new Exception("Este gênero é utilizado em livros, remova o vínculo antes de excluir o gênero.");
                Genero genero = generoOptional.get();
                service.delete(Integer.valueOf(id));
                modelAndView.getModelMap().addAttribute("msg",
                        "Gênero: ".concat(String.valueOf(genero.getCodigo()))
                                .concat(" - ").concat(genero.getDescricao())
                                .concat(" excluído com sucesso."));
            } else {
                modelAndView.getModelMap().addAttribute("error", "Gênero não encontrado: "
                        .concat(String.valueOf(id)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            modelAndView.getModelMap().addAttribute("error", ex.getMessage());
        }
        return modelAndView;
    }

    @PostMapping("/cadastro/generos")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroGeneros_POST(@Valid @ModelAttribute("genero") Genero genero,
                                             BindingResult result) {
        if (result.hasErrors())
            return cadastroGeneros_GET(genero, null, null);
        try {
            if (genero.getCodigo() != 0)
                service.update(genero);
            if (genero.getCodigo() == 0)
                service.insert(genero);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.addError(new ObjectError("error", ex.getMessage()));
            return cadastroGeneros_GET(genero, null, null);
        }
        return cadastroGeneros_GET(genero, null, null);
    }
}