package br.com.teclibrary.DAO;

import br.com.teclibrary.system.db.BasicJPA;
import br.com.teclibrary.system.db.ConnectionFactory;
import br.com.teclibrary.system.db.ModelConnection;
import br.com.teclibrary.system.db.QueryType;
import br.com.teclibrary.system.impls.ModelOptional;
import br.com.teclibrary.system.impls.ModelPair;
import org.springframework.context.annotation.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Configuration
public abstract class BasicDAO<Entidade extends Serializable, PK> extends BasicJPA {

    private Class<Entidade> persistedClass;

    protected BasicDAO(Class<Entidade> persistedClass) {
        this.persistedClass = persistedClass;
    }

    public void insert(List<Entidade> entidades) {
        for (Entidade entidade : entidades) insert(entidade);
    }

    public Entidade insert(@Valid Entidade entidade, ModelConnection modelConnection) {
        modelConnection.beginTransaction();
        modelConnection.getEntityManager().persist(entidade);
        modelConnection.getEntityManager().flush();
        modelConnection.commitTransaction();
        return entidade;
    }

    public Entidade insert(@Valid Entidade entidade) {
        ModelConnection modelConnection = ConnectionFactory.requestNewConnection();
        modelConnection.beginTransaction();
        modelConnection.getEntityManager().persist(entidade);
        modelConnection.getEntityManager().flush();
        modelConnection.commitTransaction();
        modelConnection.closeAll();
        return entidade;
    }

    public Entidade update(@Valid Entidade entidade) {
        ModelConnection modelConnection = ConnectionFactory.requestNewConnection();
        modelConnection.beginTransaction();
        modelConnection.getEntityManager().merge(entidade);
        modelConnection.getEntityManager().flush();
        modelConnection.commitTransaction();
        modelConnection.closeAll();
        return entidade;
    }

    public void delete(PK pk) {
        delete(findByPK(pk));
    }

    public void delete(Entidade entidade) {
        ModelConnection modelConnection = ConnectionFactory.requestNewConnection();
        modelConnection.beginTransaction();
        Entidade entidadeExc = modelConnection.getEntityManager().merge(entidade);
        modelConnection.getEntityManager().remove(entidadeExc);
        modelConnection.commitTransaction();
        modelConnection.closeAll();
    }

    public List<Entidade> findAll() {
        ModelConnection modelConnection = ConnectionFactory.requestNewConnection();
        Query query = modelConnection.getEntityManager().createQuery("FROM " + this.persistedClass.getName());
        List<Entidade> entidadeList = query.getResultList();
        modelConnection.closeAll();
        return entidadeList;
    }

    public List<Entidade> findAll(ModelConnection modelConnection) {
        Query query = modelConnection.getEntityManager().createQuery("FROM " + this.persistedClass.getName());
        List<Entidade> entidadeList = query.getResultList();
        return entidadeList;
    }

    public Entidade findByPK(PK pk) {
        ModelConnection modelConnection = ConnectionFactory.requestNewConnection();
        Entidade entidade = modelConnection.getEntityManager().find(this.persistedClass, pk);
        modelConnection.closeAll();
        return entidade;
    }

    public Entidade findByPK(PK pk, ModelConnection modelConnection) {
        Entidade entidade = modelConnection.getEntityManager().find(this.persistedClass, pk);
        return entidade;
    }


    public Entidade getEntidadeByQuery(String SQL, QueryType queryType, ModelPair<String, Object>... filtros) throws Exception {
        List<Entidade> entidadeList = getEntidadesByQuery(SQL, queryType, filtros);
        if (entidadeList.isEmpty()) {
            throw new NoResultException("Sem resultados para a Query: " + SQL);
        } else {
            return entidadeList.get(0);
        }
    }

    public List<Entidade> getEntidadesByQuery(String SQL, QueryType queryType, ModelPair<String, Object>... filtros) throws Exception {
        ModelConnection modelConnection = ConnectionFactory.requestNewConnection();
        ModelOptional<Exception> exception = new ModelOptional<>();
        ModelOptional<List<Entidade>> entidades = new ModelOptional<>();
        try {
            Query query = this.getQuery(modelConnection, SQL, queryType);
            for (ModelPair<String, Object> filtro : filtros)
                query.setParameter(filtro.getKey(), filtro.getValue());
            entidades.set(query.getResultList());
        } catch (Exception ex) {
            exception.set(ex);
        } finally {
            modelConnection.closeAll();
        }
        if (exception.contains())
            throw exception.get();
        else
            return entidades.get();
    }

    public Boolean checkIfExists(String SQL, ModelPair<String, Object>... filtros) throws Exception {
        Object objCount = this.retrieveStatement(SQL, QueryType.Native_Query, filtros).get(0);
        Integer count = -1;
        if(objCount instanceof BigDecimal)
            count = ((BigDecimal) objCount).intValue();
        else if(objCount instanceof BigInteger)
            count = ((BigInteger) objCount).intValue();
        return count.intValue() > 0;
    }
}
