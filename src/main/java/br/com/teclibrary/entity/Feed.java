package br.com.teclibrary.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "Feed")
@Table(name = "TFEED")
@Data
@Builder
public class Feed implements Serializable {

    @Id
    @GeneratedValue(generator = "SEQ_TFEED", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SEQ_TFEED", name = "SEQ_TFEED", allocationSize = 1)
    @Column(name = "CODFEED", nullable = false, unique = true)
    private int codigo;

    @ManyToOne(targetEntity = Livro.class, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CODLIVRO")
    private Livro livro;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CODUSU")
    private User user;

    @Length(max = 50, message = "O Resumo do Feed não pode conter mais do que 50 caracteres.")
    @NotNull(message = "O Resumo do Feed não pode ser vazio.")
    @Column(name = "RESUMOFEED", nullable = false, length = 50)
    private String resumo;

    @Length(max = 4000, message = "A Observação do Feed não pode conter mais que 4000 caracteres.")
    @NotNull(message = "A Observação do Feed não pode ser vazia.")
    @Column(name = "OBSFEED", nullable = false, length = 4000)
    private String problema;

    public Feed() {

    }

    public Feed(int codigo, Livro livro, User user, @Length(max = 50, message = "O Resumo do Feed não pode conter mais do que 50 caracteres.") @NotNull(message = "O Resumo do Feed não pode ser vazio.") String resumo, @Length(max = 4000, message = "A Observação do Feed não pode conter mais que 4000 caracteres.") @NotNull(message = "A Observação do Feed não pode ser vazia.") String problema) {
        this.codigo = codigo;
        this.livro = livro;
        this.user = user;
        this.resumo = resumo;
        this.problema = problema;
    }
}
