package br.com.teclibrary.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "Role")
@Table(name = "TROLE")
@Data
@Builder
public class Role implements Serializable {

    @Id
    @GeneratedValue(generator = "SEQ_TROLE", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_TROLE", sequenceName = "SEQ_TROLE", allocationSize = 1)
    @Column(name = "CODROLE", nullable = false, unique = true)
    private int codigo;

    @Length(max = 40, message = "A Descrição do Role não pode conter mais do que 40 caracteres")
    @NotNull(message = "A Descrição do Role não pode ser vazio.")
    @Column(name = "DESCRROLE", length = 40, nullable = false)
    private String descricao;

    public Role() {

    }

    public Role(int codigo, @Length(max = 40, message = "A Descrição do Role não pode conter mais do que 40 caracteres") @NotNull(message = "A Descrição do Role não pode ser vazio.") String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
