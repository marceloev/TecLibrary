package br.com.teclibrary.entity;

import br.com.teclibrary.entity.pk.PrecoPK;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "Preco")
@Table(name = "TPRECO")
@Data
@Builder
public class Preco implements Serializable {

    @EmbeddedId
    private PrecoPK precoPK;

    @NotNull(message = "Preço de Venda não pode ser vazio.")
    @Column(name = "PRECO", nullable = false)
    private Double preco = 0.0;

    public Preco() {

    }

    public Preco(PrecoPK precoPK, @NotNull(message = "Preço de Venda não pode ser vazio.") Double preco) {
        this.precoPK = precoPK;
        this.preco = preco;
    }

    public Preco(Livro livro, Date dataVigor, @NotNull(message = "Preço de Venda não pode ser vazio.") Double preco) {
        this.precoPK = new PrecoPK(livro, dataVigor);
        this.preco = preco;
    }
}