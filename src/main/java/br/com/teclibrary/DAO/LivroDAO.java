package br.com.teclibrary.DAO;

import br.com.teclibrary.entity.Livro;
import br.com.teclibrary.system.db.ModelConnection;
import br.com.teclibrary.system.db.QueryType;
import br.com.teclibrary.system.db.SystemParams;
import br.com.teclibrary.system.impls.ModelPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LivroDAO extends BasicDAO<Livro, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(LivroDAO.class);
    private static int LIVROS_ROTATIVIDADE = Integer.valueOf(
            SystemParams.getParametro("teclibrary-app.livros-rotatividade-dias"));

    protected LivroDAO() {
        super(Livro.class);
    }

    public List<Livro> findRecentes() throws Exception {
        LocalDateTime localDataFiltro = Timestamp.from(Instant.now()).toLocalDateTime().minusDays(LIVROS_ROTATIVIDADE);
        Date dataFiltro = Date.from(localDataFiltro.atZone(ZoneId.systemDefault()).toInstant());
        return this.getEntidadesByQuery("Livro.findByRecentes", QueryType.Named_Query,
                new ModelPair("P_DATAFILTRO", dataFiltro));
    }

    public List<Livro> findBySearch(String search) throws Exception {
        return this.getEntidadesByQuery("Livro.findBySearch", QueryType.Named_Query,
                new ModelPair("P_SEARCH", "%".concat(search).concat("%")));
    }

    public List<Livro> findByGenero(Integer ID) throws Exception {
        final String SQL = "SELECT L FROM Livro L JOIN L.generos G WHERE G.codigo = :P_CODGENERO";
        List<Livro> livroList = this.getEntidadesByQuery(SQL, QueryType.Normal_Query,
                new ModelPair("P_CODGENERO", ID));
        return livroList;
    }

    public List<Livro> findBookLookALike(Livro livro, ModelConnection connection) throws Exception {
        final String SQL = "SELECT JT.CODLIVRO FROM JT_LIVRO_GENEROS JT\n" +
                "WHERE JT.CODLIVRO <> :P_CODLIVRO AND JT.CODGENERO IN \n" +
                "(SELECT CODGENERO FROM JT_LIVRO_GENEROS JT2 WHERE JT2.CODLIVRO = :P_CODLIVRO)\n" +
                "GROUP BY JT.CODLIVRO ORDER BY COUNT(1) DESC";
        List<Integer> integerList = this.retrieveStatement(connection, SQL, QueryType.Native_Query,
                new ModelPair("P_CODLIVRO", livro.getCodigo()));
        List<Livro> livroList = new ArrayList<>();
        for (Integer integer : integerList) livroList.add(this.findByPK(integer, connection));
        return livroList;
    }
}
