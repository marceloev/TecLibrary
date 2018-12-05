package br.com.teclibrary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
public class ExceptionCtrl {

    @GetMapping("/403")
    public ModelAndView errorPage_403() {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.getModelMap().addAttribute("code", 403);
        modelAndView.getModelMap().addAttribute("message",
                "Você fez uma solicitação em uma área onde você não tem permissões.");
        return modelAndView;
    }
}