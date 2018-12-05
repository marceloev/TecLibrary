package br.com.teclibrary.DAO;

import br.com.teclibrary.entity.Preco;
import br.com.teclibrary.entity.pk.PrecoPK;

public class PrecoDAO extends BasicDAO<Preco, PrecoPK> {

    public PrecoDAO() {
        super(Preco.class);
    }
}
