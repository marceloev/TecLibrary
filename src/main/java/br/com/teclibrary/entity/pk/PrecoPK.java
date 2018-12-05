package br.com.teclibrary.entity.pk;

import br.com.teclibrary.entity.Livro;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PrecoPK implements Serializable {

    @ManyToOne(targetEntity = Livro.class, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CODLIVRO")
    private Livro livro;

    @Column(name = "DTVIGOR", nullable = false)
    private Date dataVigor;

    public PrecoPK() {

    }

    public PrecoPK(Livro livro, Date dataVigor) {
        this.livro = livro;
        this.dataVigor = dataVigor;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Date getDataVigor() {
        return dataVigor;
    }

    public void setDataVigor(Date dataVigor) {
        this.dataVigor = dataVigor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrecoPK precoPK = (PrecoPK) o;
        return Objects.equals(livro, precoPK.livro) &&
                Objects.equals(dataVigor, precoPK.dataVigor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(livro, dataVigor);
    }

    @Override
    public String toString() {
        return "PrecoPK{" +
                "livro=" + livro +
                ", dataVigor=" + dataVigor +
                '}';
    }
}