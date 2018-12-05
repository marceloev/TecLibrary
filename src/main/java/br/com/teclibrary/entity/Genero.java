package br.com.teclibrary.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "Genero")
@Table(name = "TGENERO")
@Data
@Builder
public class Genero implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TGENERO")
    @SequenceGenerator(name="SEQ_TGENERO", sequenceName = "SEQ_TGENERO", allocationSize = 1)
    @Column(name = "CODGENERO", unique = true, nullable = false)
    private int codigo;

    @Length(max = 30, message = "A Descrição do Gênero deve conter até 30 dígitos.")
    @NotNull(message = "A Descrição do Gênero não pode ser vazia.")
    @Column(name = "DESCRGENERO", length = 30, nullable = false)
    private String descricao;

    public Genero() {}

    public Genero(int codigo, @Length(max = 30, message = "A Descrição do Gênero deve conter até 30 dígitos.") @NotNull(message = "A Descrição do Gênero não pode ser vazia.") String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}