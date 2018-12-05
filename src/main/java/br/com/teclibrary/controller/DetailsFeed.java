package br.com.teclibrary.controller;

import br.com.teclibrary.entity.Feed;
import br.com.teclibrary.entity.User;
import br.com.teclibrary.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class DetailsFeed {

    @Autowired
    private FeedService service;

    @GetMapping("/detalhes-feed")
    public ModelAndView detalhesFeed_GET(@AuthenticationPrincipal User user) throws Exception {
        ModelAndView modelAndView = new ModelAndView("detalhesFeed");
        if (user.getRole().getDescricao().equals("USER")) {
            List<Feed> feedList = service.findFeedByUser(user.getCodigo());
            modelAndView.getModelMap().addAttribute("feeds", feedList);
            return modelAndView;
        } else {
            List<Feed> feedList = service.findAll();
            modelAndView.getModelMap().addAttribute("feeds", feedList);
            return modelAndView;
        }
    }
}
