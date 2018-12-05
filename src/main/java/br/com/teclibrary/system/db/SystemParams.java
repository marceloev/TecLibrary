package br.com.teclibrary.system.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class SystemParams {

    private static final Logger logger = LoggerFactory.getLogger(SystemParams.class);
    private static final Properties properties = new Properties();

    static {
        try {
            InputStream inputStream = SystemParams.class.getResourceAsStream("/static/properties/parametros.properties");
            properties.load(inputStream);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            System.exit(1);
        }
    }

    public static String getParametro(String key) {
        logger.info("Solicitando parâmetro: " + key);
        String value = properties.getProperty(key);
        logger.info("Retorno: " + value);
        return value;
    }

    public static Properties getProperties() {
        return properties;
    }
}
