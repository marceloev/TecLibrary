package br.com.teclibrary.controller;

import br.com.teclibrary.entity.Livro;
import br.com.teclibrary.service.GeneroService;
import br.com.teclibrary.service.LivroService;
import br.com.teclibrary.system.db.ConnectionFactory;
import br.com.teclibrary.system.repository.FileType;
import br.com.teclibrary.system.repository.RepositoryCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

@Controller
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LivroCtrl {

    @Autowired
    private LivroService service;
    @Autowired
    private GeneroService generoService;

    @GetMapping("/livro/{codigo}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView detailsLivro_GET(@PathVariable(name = "codigo", required = false) String codigo) {
        try {
            //Necessário pois, o SPRING reaproveita a sessão
            Livro livro = service.findByPK(Integer.valueOf(codigo), ConnectionFactory.requestNewConnection(20));
            if (livro == null)
                throw new Exception("Livro não encontrado, código: ".concat(codigo));
            else
                return cadastroLivros_GET(livro);
        } catch (Exception ex) {
            ex.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("redirect:/home");
            modelAndView.getModelMap().addAttribute("error", ex.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("delete/livros/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView deleteLivros_GET(@PathVariable("id") String id) {
        ModelAndView modelAndView = cadastroLivros_GET(new Livro());
        try {
            Livro livro = service.findByPK(Integer.valueOf(id));
            if (livro == null) {
                throw new Exception("Livro não encontrado, código: ".concat(id));
            } else {
                service.delete(Integer.valueOf(id));
                modelAndView.getModelMap().addAttribute("msg",
                        "Livro: ".concat(String.valueOf(livro.getCodigo()))
                                .concat(" - ").concat(livro.getDescricao())
                                .concat(" excluído com sucesso."));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            modelAndView.getModelMap().addAttribute("error", ex.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("/cadastro/livros")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroLivros_GET(Livro livro) {
        ModelAndView modelAndView = new ModelAndView("cadastrarLivro");
        modelAndView.getModelMap().addAttribute("livro", livro);
        modelAndView.getModelMap().addAttribute("generos", generoService.findAll());
        return modelAndView;
    }

    @PostMapping("/cadastro/livros")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroLivros_POST(@RequestParam("imgInp") MultipartFile imgFile,
                                            @RequestParam("pdfInp") MultipartFile pdfFile,
                                            @Valid @ModelAttribute("livro") Livro livro,
                                            BindingResult result) {
        final Boolean inserting = livro.getCodigo() == 0;
        final Boolean hasImage = (imgFile != null && !imgFile.isEmpty());
        final Boolean hasPDF = (pdfFile != null && !pdfFile.isEmpty());
        try {
            if (!inserting) { //Está editando
                service.update(livro);
                if (hasImage) RepositoryCtrl.mergeFileToDatabase("/imgs", imgFile, livro, FileType.Image);
            } else {
                if (!hasImage)
                    throw new Exception("A Foto do Livro não pode ser vazia.");
                service.insert(livro);
                RepositoryCtrl.mergeFileToDatabase("/imgs", imgFile, livro, FileType.Image);
            }
            if (hasPDF)
                RepositoryCtrl.mergeFileToDatabase("/pdfs", pdfFile, livro, FileType.PDF);
            ModelAndView modelAndView = cadastroLivros_GET(livro);
            modelAndView.getModelMap().addAttribute("msg",
                    String.format("Livro %s - %s %s com sucesso.",
                            String.valueOf(livro.getCodigo()),
                            livro.getDescricao(),
                            (inserting ? "cadastrado" : "editado")));
            return modelAndView;
        } catch (Exception ex) {
            ex.printStackTrace();
            result.addError(new ObjectError("error", ex.getMessage()));
            return cadastroLivros_GET(livro);
        }
    }
}