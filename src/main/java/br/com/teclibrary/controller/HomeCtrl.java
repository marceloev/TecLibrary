package br.com.teclibrary.controller;

import br.com.teclibrary.entity.Genero;
import br.com.teclibrary.entity.Livro;
import br.com.teclibrary.service.GeneroService;
import br.com.teclibrary.service.LivroService;
import br.com.teclibrary.system.impls.ModelHomeMV;
import br.com.teclibrary.system.impls.ModelOptional;
import br.com.teclibrary.system.preco.PrecoRetriever;
import br.com.teclibrary.system.repository.FileType;
import br.com.teclibrary.system.repository.ModelFile;
import br.com.teclibrary.system.repository.RepositoryCtrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class HomeCtrl {

    private static final Logger logger = LoggerFactory.getLogger(HomeCtrl.class);

    @Autowired
    private LivroService service;
    @Autowired
    private GeneroService generoService;

    @RequestMapping("/home")
    private ModelAndView home_GET(@RequestParam(value = "search", required = false) String search,
                                  @RequestParam(value = "error", required = false) String error) {
        ModelAndView mv = new ModelHomeMV();
        if (error != null) mv.getModelMap().addAttribute("error", error);
        mv.getModelMap().addAttribute("search", search);
        if (search == null || search.trim().isEmpty()) {
            try {
                mv.getModelMap().addAttribute("livroList", service.findRecentes().stream().
                        sorted(Comparator.comparing(Livro::getCodigo)).collect(Collectors.toList()));
            } catch (Exception ex) {
                ex.printStackTrace();
                mv.getModelMap().addAttribute("error", ex);
            }
        } else if (search != null) {
            try {
                mv.getModelMap().addAttribute("livroList", service.findBySearch(search).stream().
                        sorted(Comparator.comparing(Livro::getCodigo)).collect(Collectors.toList()));
            } catch (Exception ex) {
                ex.printStackTrace();
                mv.getModelMap().addAttribute("error", ex);
            }
        }
        return mv;
    }

    @RequestMapping("/genero/{id}")
    private ModelAndView genero_GET(@PathVariable("id") String ID) throws Exception {
        ModelAndView modelAndView = new ModelHomeMV();
        Genero genero = generoService.findByPK(Integer.valueOf(ID));
        if (genero != null) {
            modelAndView.getModelMap().addAttribute("livroList", service.findByGenero(Integer.valueOf(ID)));
            String info = "Filtrando livros pelo Gênero: ".concat(genero.getDescricao());
            modelAndView.getModelMap().addAttribute("info", info);
        } else {
            modelAndView.getModelMap().addAttribute("livroList", service.findRecentes());
            modelAndView.getModelMap().addAttribute("error",
                    String.format("Gênero de código %s não encontrado", ID));
        }
        return modelAndView;
    }

    @GetMapping("/imagens/{ID}")
    private ResponseEntity<byte[]> getImage(@PathVariable("ID") String ID) {
        ModelOptional<ModelFile> fileModelOptional = new ModelOptional<>();
        fileModelOptional.set(RepositoryCtrl.retrieveArquivo("/imgs", ID, FileType.Image));
        if (!fileModelOptional.contains()) {
            String errorMSG = String.format("Ocorreu um erro na tentativa de recuperar a imagem do livro " +
                    "%s, o arquivo de imagem retornou vazio. Iremos inserir a imagem DEFAULT neste caso", ID);
            logger.error(errorMSG);
            InputStream inputStreamNoImage = this.getClass().getResourceAsStream("/static/imgs/noImage.png");
            fileModelOptional.set(new ModelFile("0", inputStreamNoImage));
        }
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(fileModelOptional.get().transformToByteA(), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/pdfs/{ID}")
    private ResponseEntity<byte[]> getPDF(@PathVariable("ID") String ID) {
        ModelFile modelFile = RepositoryCtrl.retrieveArquivo("/pdfs", ID, FileType.PDF);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(modelFile.transformToByteA(), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/clear-file-cache")
    private ModelAndView clearFileCache_GET() {
        RepositoryCtrl.getDropboxApi().clearCache(null);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/clear-preco-cache")
    private ModelAndView clearPrecoCache_GET() {
        PrecoRetriever.remakeAllCache(new ModelOptional<>());
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/clear-cache")
    private ModelAndView clearCache_GET() {
        RepositoryCtrl.getDropboxApi().clearCache(null);
        PrecoRetriever.remakeAllCache(new ModelOptional<>());
        return new ModelAndView("redirect:/home");
    }

}
