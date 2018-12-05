package br.com.teclibrary.system.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.Objects;

public class ModelConnection {

    private final Logger logger = LoggerFactory.getLogger(ModelConnection.class);
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;
    private final Date dhAbertura;
    private final int timeOut;

    public ModelConnection(EntityManager entityManager, EntityManagerFactory entityManagerFactory, int timeOut) {
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.dhAbertura = new Date();
        this.timeOut = timeOut;
        logger.info("Iniciando abertura de nova connectionFactory: :" + this.toString());
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Date getDhAbertura() {
        return dhAbertura;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void beginTransaction() {
        this.getEntityManager().getTransaction().begin();
    }

    public void commitTransaction() {
        this.getEntityManager().getTransaction().commit();
    }

    public void rollbackTransaction() {
        this.getEntityManager().getTransaction().rollback();
    }

    public void closeAll() {
        logger.info("Foi solicitado o close da conexão: " + this.toString());
        this.getEntityManagerFactory().close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelConnection)) return false;
        ModelConnection that = (ModelConnection) o;
        return getTimeOut() == that.getTimeOut() &&
                Objects.equals(getEntityManager(), that.getEntityManager()) &&
                Objects.equals(getDhAbertura(), that.getDhAbertura());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntityManager(), getDhAbertura(), getTimeOut());
    }

    @Override
    public String toString() {
        return "ConnectionModel{" +
                "entityManager=" + entityManager +
                ", dhAbertura=" + dhAbertura +
                ", timeOut=" + timeOut +
                '}';
    }
}
