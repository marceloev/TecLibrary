package br.com.teclibrary.DAO;

import br.com.teclibrary.entity.Genero;
import br.com.teclibrary.system.impls.ModelPair;

public class GeneroDAO extends BasicDAO<Genero, Integer> {

    public GeneroDAO() {
        super(Genero.class);
    }

    public Boolean isGeneroUsed(Integer ID) throws Exception{
        return this.checkIfExists("SELECT COUNT(1) AS CONT FROM JT_LIVRO_GENEROS WHERE CODGENERO = :P_CODGENERO",
                new ModelPair("P_CODGENERO", ID));
    }
}
