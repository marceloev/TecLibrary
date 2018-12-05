package br.com.teclibrary.system.converter;

import br.com.teclibrary.entity.Genero;
import br.com.teclibrary.service.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GeneroConverter implements Converter<String, Genero> {

    @Autowired
    private GeneroService service;

    @Override
    public Genero convert(String ID) {
        return service.findByPK(Integer.valueOf(ID));
    }
}
