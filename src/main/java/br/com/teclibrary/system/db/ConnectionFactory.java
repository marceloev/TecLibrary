package br.com.teclibrary.system.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class ConnectionFactory {

    private static final List<ModelConnection> LIST_CONNECTIONS = new ArrayList<>();
    private static final int DEFAULT_TIMEOUT_CONNECTION = 60; //SEGUNDOS
    public static DefaultPersistenceUnit DEFAULT_PERSISTENCE_UNIT = DefaultPersistenceUnit.Web;
    public static Boolean isLocal = false;

    public static ModelConnection requestNewConnection() {
        return requestNewConnection(getDefaultPersistenceUnit(), DEFAULT_TIMEOUT_CONNECTION);
    }

    public static ModelConnection requestNewConnection(String PERSISTENCE_UNIT, int TIMEOUT) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        if (TIMEOUT == 0 || TIMEOUT == -1) TIMEOUT = 99999;
        ModelConnection modelConnection = new ModelConnection(em, emf, TIMEOUT);
        new CloseConnectionTask(modelConnection, TIMEOUT);
        return modelConnection;
    }

    public static ModelConnection requestNewConnection(int TIMEOUT) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(getDefaultPersistenceUnit());
        EntityManager em = emf.createEntityManager();
        if (TIMEOUT == 0 || TIMEOUT == -1) TIMEOUT = 99999;
        ModelConnection modelConnection = new ModelConnection(em, emf, TIMEOUT);
        new CloseConnectionTask(modelConnection, TIMEOUT);
        return modelConnection;
    }

    private static final String getDefaultPersistenceUnit() {
        return DEFAULT_PERSISTENCE_UNIT.getValue();
    }

    public static void setDefaultPersistenceUnit(DefaultPersistenceUnit defaultPersistenceUnit) {
        DEFAULT_PERSISTENCE_UNIT = defaultPersistenceUnit;
    }

    public enum DefaultPersistenceUnit {
        Web("TecLibrary"),
        Local("TecLibrary_Local"),
        Sankhya("TecLibrary_Sankhya");

        private String value;

        DefaultPersistenceUnit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
