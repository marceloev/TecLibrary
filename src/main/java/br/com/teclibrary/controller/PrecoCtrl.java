package br.com.teclibrary.controller;


import br.com.teclibrary.entity.Livro;
import br.com.teclibrary.entity.Preco;
import br.com.teclibrary.service.LivroService;
import br.com.teclibrary.service.PrecoService;
import br.com.teclibrary.system.impls.ModelOptional;
import br.com.teclibrary.system.preco.ListPreco;
import br.com.teclibrary.system.preco.PrecoProduto;
import br.com.teclibrary.system.preco.PrecoRetriever;
import br.com.teclibrary.system.response.ModelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PrecoCtrl {

    private static final SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Autowired
    private LivroService livroService;
    @Autowired
    private PrecoService precoService;

    @GetMapping("/cadastro/precos")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroPrecos_GET(ModelResponse response) {
        ModelAndView modelAndView = new ModelAndView("cadastrarPreco");
        if (response.getResponseType() == ModelResponse.ResponseType.Error)
            modelAndView.getModelMap().addAttribute("error", response.getMensagem());
        else if (!response.getMensagem().isEmpty())
            modelAndView.getModelMap().addAttribute("msg", response.getMensagem());
        ListPreco listPreco = new ListPreco();
        for (Livro livro : livroService.findAll()) {
            listPreco.getPrecoProdutoList().add(
                    new PrecoProduto(livro.getCodigo(),
                            livro.getDescricao(),
                            livro.getCodigoBarra()));
        }
        modelAndView.getModelMap().addAttribute("PrecoList", listPreco);
        return modelAndView;
    }

    @PostMapping(value = "/cadastro/precos")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ModelAndView cadastroPrecos_POST(@ModelAttribute("PrecoList") ListPreco listPreco) {
        ModelResponse response = new ModelResponse();
        for (PrecoProduto precoProduto : listPreco.getPrecoProdutoList()) {
            try {
                if (precoProduto.getPreco() == null) precoProduto.setPreco("0.0");
                String tmpPreco = precoProduto.getPreco()
                        .replaceAll("[^0-9||.||,]", "")
                        .replaceAll(",", ".");
                precoProduto.setPrecoDbl(Double.parseDouble(tmpPreco));
            } catch (Exception ex) {
                ex.printStackTrace();
                response.setMensagem(ex.getMessage());
                response.setResponseType(ModelResponse.ResponseType.Error);
            }
        }
        if (response.getResponseType() == ModelResponse.ResponseType.None) {
            Date dataVigor = new Date();
            List<Preco> precoList = new ArrayList<>();
            for (PrecoProduto precoProduto : listPreco.getPrecoProdutoList()) {
                if (precoProduto.getPreco() == "0.0") continue;
                Preco preco = new Preco(
                        livroService.findByPK(precoProduto.getCodigo()),
                        dataVigor,
                        precoProduto.getPrecoDbl());
                precoList.add(preco);
            }
            precoService.insert(precoList);
            response.setResponseType(ModelResponse.ResponseType.Success);
            response.setMensagem("Preços atualizados com sucesso, nova data de vigor: ".concat(fmt.format(dataVigor)));
            PrecoRetriever.remakeAllCache(new ModelOptional<>());
        }
        return cadastroPrecos_GET(response);
    }
}
