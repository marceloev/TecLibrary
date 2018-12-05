package br.com.teclibrary.system.db;

import br.com.teclibrary.system.impls.ModelOptional;
import br.com.teclibrary.system.impls.ModelPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.util.List;

public class BasicJPA {

    private final Logger logger = LoggerFactory.getLogger(BasicJPA.class);
    private ModelConnection modelConnection;

    public int executeStatement(String SQL, QueryType queryType, ModelPair<String, Object>... filtros) throws Exception {
        ModelOptional<Integer> rowsAffected = new ModelOptional<>();
        ModelOptional<Exception> exception = new ModelOptional<>();
        this.setModelConnection(ConnectionFactory.requestNewConnection());
        try {
            this.getModelConnection().beginTransaction();
            Query query = getQuery(this.getModelConnection(), SQL, queryType);
            for (ModelPair<String, Object> filtro : filtros)
                query.setParameter(filtro.getKey(), filtro.getValue());
            rowsAffected.set(query.executeUpdate());
            this.getModelConnection().commitTransaction();
        } catch (Exception ex) {
            exception.set(ex);
            logger.error(ex.getMessage(), ex);
        } finally {
            this.getModelConnection().closeAll();
        }
        if (exception.contains())
            throw exception.get();
        else
            return rowsAffected.get();
    }

    public List retrieveStatement(ModelConnection connection, String SQL, QueryType queryType, ModelPair<String, Object>... filtros) throws Exception {
        ModelOptional<List> resultSet = new ModelOptional<>();
        ModelOptional<Exception> exception = new ModelOptional<>();
        this.setModelConnection(connection);
        try {
            this.getModelConnection().beginTransaction();
            Query query = getQuery(this.getModelConnection(), SQL, queryType);
            for (ModelPair<String, Object> filtro : filtros)
                query.setParameter(filtro.getKey(), filtro.getValue());
            resultSet.set(query.getResultList());
            this.getModelConnection().commitTransaction();
        } catch (Exception ex) {
            exception.set(ex);
            logger.error(ex.getMessage(), ex);
        }
        if (exception.contains())
            throw exception.get();
        else
            return resultSet.get();
    }

    public List retrieveStatement(String SQL, QueryType queryType, ModelPair<String, Object>... filtros) throws Exception {
        ModelOptional<List> resultSet = new ModelOptional<>();
        ModelOptional<Exception> exception = new ModelOptional<>();
        this.setModelConnection(ConnectionFactory.requestNewConnection());
        try {
            this.getModelConnection().beginTransaction();
            Query query = getQuery(this.getModelConnection(), SQL, queryType);
            for (ModelPair<String, Object> filtro : filtros)
                query.setParameter(filtro.getKey(), filtro.getValue());
            resultSet.set(query.getResultList());
            this.getModelConnection().commitTransaction();
        } catch (Exception ex) {
            exception.set(ex);
            logger.error(ex.getMessage(), ex);
        } finally {
            this.getModelConnection().closeAll();
        }
        if (exception.contains())
            throw exception.get();
        else
            return resultSet.get();
    }

    public Query getQuery(ModelConnection modelConnection, String SQL, QueryType queryType) throws Exception {
        switch (queryType) {
            case Normal_Query:
                return modelConnection.getEntityManager()
                        .createQuery(SQL);
            case Named_Query:
                return modelConnection.getEntityManager()
                        .createNamedQuery(SQL);
            case Native_Query:
                return modelConnection.getEntityManager()
                        .createNativeQuery(SQL);
            default:
                throw new Exception("BasicJPA.getQuery(" + queryType + ") não suportado.");
        }
    }

    public ModelConnection getModelConnection() {
        return modelConnection;
    }

    public void setModelConnection(ModelConnection modelConnection) {
        this.modelConnection = modelConnection;
    }
}
