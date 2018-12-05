package br.com.teclibrary.system.preco;

import br.com.teclibrary.entity.Livro;
import br.com.teclibrary.service.LivroService;
import br.com.teclibrary.service.PrecoService;
import br.com.teclibrary.system.db.ConnectionFactory;
import br.com.teclibrary.system.db.QueryType;
import br.com.teclibrary.system.impls.ModelOptional;
import br.com.teclibrary.system.impls.ModelPair;
import br.com.teclibrary.system.task.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrecoRetriever {

    private static final DecimalFormat PRECO_FORMATTER = new DecimalFormat("#,##0.00");
    private static final PrecoService PRECO_SERVICE = new PrecoService();
    private static final List<PrecoCache> PRECO_CACHE_LIST = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(PrecoRetriever.class);

    public static final String getFmtPrecoBruto(Integer ID) {
        if (isCached(ID))
            return getPrecoFormatter().format(getCachedPreco(ID).getVlrBruto());
        return getPrecoFormatter().format(getPrecoBruto(ID));
    }

    public static final String getFmtPrecoLiquido(Integer ID) {
        if (isCached(ID))
            return getPrecoFormatter().format(getCachedPreco(ID).getVlrLiquido());
        return getPrecoFormatter().format(getPrecoLiquido(ID));
    }

    public static Double getPrecoBruto(Integer ID) {
        ModelOptional<Double> preco = new ModelOptional<>();
        try {
            preco.set((Double) getPrecoService().retrieveStatement("SELECT COALESCE(MAX(PRC.PRECO), 0.0)\n" +
                            "FROM TPRECO PRC WHERE PRC.CODLIVRO = :P_CODLIVRO\n" +
                            "AND PRC.DTVIGOR = (SELECT MAX(DTVIGOR) FROM TPRECO PRC2 WHERE PRC.CODLIVRO = PRC2.CODLIVRO)",
                    QueryType.Native_Query, new ModelPair("P_CODLIVRO", ID)).get(0));
        } catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
            preco.set(0.00);
        }
        return new BigDecimal(preco.get()).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static Double getPrecoLiquido(Integer ID) {
        ModelOptional<Double> preco = new ModelOptional<>();
        try {
            Double precoBruto = getPrecoBruto(ID);
            List<Object[]> pairs = getPrecoService().retrieveStatement("SELECT DESCVLRPROMO, PERCVLRPROMO FROM TPROMOCAO PRM\n" +
                            "WHERE (PRM.CODLIVRO = :P_CODLIVRO OR PRM.CODGENERO IN (SELECT JT.CODGENERO  FROM JT_LIVRO_GENEROS JT WHERE JT.CODLIVRO = :P_CODLIVRO))\n" +
                            "AND DTINIPROMO <= NOW() AND DTFIMPROMO >= NOW() AND ATIVO = 'S'",
                    QueryType.Native_Query, new ModelPair<>("P_CODLIVRO", ID));
            if (pairs == null || pairs.isEmpty())
                return new BigDecimal(precoBruto).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            else
                return subtractToPreco(precoBruto, pairs);
        } catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
            preco.set(0.00);
            return preco.get();
        }
    }

    private static Double subtractToPreco(Double precoBruto, List<Object[]> promocoes) {
        Double precoFinal = precoBruto;
        for (Object[] promocao : promocoes) {
            if ((Double) promocao[0] != 0.0)
                precoFinal = precoFinal - (Double) promocao[0];
            if ((Double) promocao[1] != 0.0)
                precoFinal = precoFinal * ((100.0 - (Double) promocao[1]) / 100);
        }
        return new BigDecimal(precoFinal).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    private static Boolean isCached(Integer ID) {
        if (getPrecoCacheList().isEmpty()) return false;
        return getPrecoCacheList().stream()
                .filter(precoCache -> precoCache.getPrecoProduto().getCodigo() == ID).count() > 0;
    }

    public static void clearCache() {
        getPrecoCacheList().clear();
    }

    public static PrecoCache getCachedPreco(Integer ID) {
        return getPrecoCacheList().stream()
                .filter(precoCache -> precoCache.getPrecoProduto().getCodigo() == ID).findFirst().get();
    }

    public static void remakeAllCache(ModelOptional<PrecoCache> precoCacheModelOptional) {
        if (precoCacheModelOptional == null || precoCacheModelOptional.contains()) {
            getPrecoCacheList().add(precoCacheModelOptional.get());
        } else {
            clearCache();
            getLogger().info("Iniciando método de criação de cache para a rotina de preços do sistema.");
            new AsyncTask(() -> {
                LivroService livroService = new LivroService();
                for (Livro livro : livroService.findAll(ConnectionFactory.requestNewConnection(180))) {
                    try {
                        PrecoProduto precoProduto = new PrecoProduto(
                                livro.getCodigo(),
                                livro.getDescricao(),
                                livro.getCodigoBarra(),
                                false
                        );
                        PrecoCache precoCache = new PrecoCache(precoProduto,
                                getPrecoBruto(livro.getCodigo()),
                                getPrecoLiquido(livro.getCodigo()));
                        getPrecoCacheList().add(precoCache);
                        String infoMSG = String.format("Livro %s cacheado com sucesso.", precoCache.toString());
                        getLogger().info(infoMSG);
                    } catch (Exception ex) {
                        getLogger().error("Rotina de cache de preços: " + ex.getMessage(), ex);
                        remakeAllCache(new ModelOptional<>());
                    }
                }
            }).trigger();
        }
    }

    public static DecimalFormat getPrecoFormatter() {
        return PRECO_FORMATTER;
    }

    public static PrecoService getPrecoService() {
        return PRECO_SERVICE;
    }

    public static List<PrecoCache> getPrecoCacheList() {
        return PRECO_CACHE_LIST;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
