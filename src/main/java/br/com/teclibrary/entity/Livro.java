package br.com.teclibrary.entity;

import br.com.teclibrary.system.preco.PrecoRetriever;
import br.com.teclibrary.system.repository.FileType;
import br.com.teclibrary.system.repository.RepositoryCtrl;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "Livro")
@NamedQueries({
        @NamedQuery(name = "Livro.findByRecentes",
                query = "SELECT L FROM Livro L WHERE L.dataCadastro >= :P_DATAFILTRO"),
        @NamedQuery(name = "Livro.findBySearch",
                query = "SELECT L FROM Livro L " +
                        "WHERE UNACCENT(UPPER(L.codigoBarra)) LIKE UNACCENT(UPPER(:P_SEARCH))\n" +
                        "OR UNACCENT(UPPER(L.descricao)) LIKE UNACCENT(UPPER(:P_SEARCH))\n" +
                        "OR UNACCENT(UPPER(L.resenha)) LIKE UNACCENT(UPPER(:P_SEARCH))\n" +
                        "OR UNACCENT(UPPER(L.sinopse)) LIKE UNACCENT(UPPER(:P_SEARCH))")
})
@Table(name = "TLIVRO")
@Data
@Builder
public class Livro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TLIVRO")
    @SequenceGenerator(name = "SEQ_TLIVRO", sequenceName = "SEQ_TLIVRO", allocationSize = 1)
    @Column(name = "CODLIVRO", unique = true, nullable = false)
    private int codigo;

    @Length(max = 20, message = "O Código de Barras do Livro deve conter até 20 dígitos.")
    @Column(name = "CODBARRAS", length = 20)
    private String codigoBarra;

    @Length(max = 60, message = "A Descrição do Livro deve conter até 35 dígitos.")
    @NotNull(message = "A Descrição do Livro não pode ser vazia.")
    @Column(name = "DESCRLIVRO", length = 60, nullable = false)
    private String descricao;

    @Length(max = 255, message = "A Resenha do Livro deve conter até 255 dígitos.")
    @Column(name = "RESENHA")
    private String resenha;

    @Length(max = 1000, message = "A Sinopse do Livro deve conter até 1000 dígitos")
    @Column(name = "SINOPSE", length = 1000)
    private String sinopse;

    @NotNull(message = "A Data de Publicação do Livro não pode ser vazia.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DHPUBLICACAO", nullable = false)
    private Date dataPublicacao;

    @NotNull(message = "A Data de Cadastro do Livro não pode ser vazia.")
    @Column(name = "DHCADASTRO", nullable = false)
    private Date dataCadastro = new Date();

    @ManyToMany
    @JoinTable(name = "JT_LIVRO_GENEROS", joinColumns =
            {@JoinColumn(name = "CODLIVRO")}, inverseJoinColumns =
            {@JoinColumn(name = "CODGENERO")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Genero> generos;

    public Livro() {

    }

    public Livro(int codigo, @Length(max = 20, message = "O Código de Barras do Livro deve conter até 20 dígitos.") String codigoBarra, @Length(max = 60, message = "A Descrição do Livro deve conter até 35 dígitos.") @NotNull(message = "A Descrição do Livro não pode ser vazia.") String descricao, @Length(max = 255, message = "A Resenha do Livro deve conter até 255 dígitos.") String resenha, @Length(max = 1000, message = "A Sinopse do Livro deve conter até 1000 dígitos") String sinopse, @NotNull(message = "A Data de Publicação do Livro não pode ser vazia.") Date dataPublicacao, @NotNull(message = "A Data de Cadastro do Livro não pode ser vazia.") Date dataCadastro, List<Genero> generos) {
        this.codigo = codigo;
        this.codigoBarra = codigoBarra;
        this.descricao = descricao;
        this.resenha = resenha;
        this.sinopse = sinopse;
        this.dataPublicacao = dataPublicacao;
        this.dataCadastro = dataCadastro;
        this.generos = generos;
    }

    public Character hasPDF() {
        Boolean exists = RepositoryCtrl.getDropboxApi().fileExists("/pdfs", this.getCodigo(), FileType.PDF);
        return (exists ? 'S' : 'N');
    }

    public String getPreco() {
        return PrecoRetriever.getFmtPrecoLiquido(this.getCodigo());
    }
}