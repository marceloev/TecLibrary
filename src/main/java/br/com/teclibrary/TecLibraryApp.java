package br.com.teclibrary;

import br.com.teclibrary.system.db.ConnectionFactory;
import br.com.teclibrary.system.db.DBRules;
import br.com.teclibrary.system.impls.ModelOptional;
import br.com.teclibrary.system.preco.PrecoRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TecLibraryApp {

    private static final Logger logger = LoggerFactory.getLogger(TecLibraryApp.class);

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-addon-server-local"))
           ConnectionFactory.setDefaultPersistenceUnit(ConnectionFactory.DefaultPersistenceUnit.Local);
        else if (args.length > 0 && args[0].equals("-addon-server-sankhya"))
            ConnectionFactory.setDefaultPersistenceUnit(ConnectionFactory.DefaultPersistenceUnit.Sankhya);
        SpringApplication.run(TecLibraryApp.class, args);
        DBRules.init();
        PrecoRetriever.remakeAllCache(new ModelOptional());
    }

    public static final void setPersistenceUnit(ConnectionFactory.DefaultPersistenceUnit defaultPersistenceUnit) {
        ConnectionFactory.setDefaultPersistenceUnit(defaultPersistenceUnit);
        logger.warn("Utilizando a PersistenceUnit: " + defaultPersistenceUnit.getValue());
    }
}
