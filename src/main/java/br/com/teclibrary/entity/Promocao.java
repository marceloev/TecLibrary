package br.com.teclibrary.entity;

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
import java.util.Objects;

@Entity(name = "Promocao")
@Table(name = "TPROMOCAO")
@Data
@Builder
public class Promocao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TPROMOCAO")
    @SequenceGenerator(name = "SEQ_TPROMOCAO", sequenceName = "SEQ_TPROMOCAO", allocationSize = 1)
    @Column(name = "CODPROMO")
    private int codigo;

    @ManyToOne(targetEntity = Livro.class, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CODLIVRO")
    private Livro livro;

    @ManyToOne(targetEntity = Genero.class, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CODGENERO")
    private Genero genero;

    @NotNull(message = "A Descrição da Promoção não pode ser vazia.")
    @Length(max = 60, message = "A Descrição da Promoção deve conter até 60 dígitos.")
    @Column(name = "DESCRPROMO", nullable = false, length = 60)
    private String nomePromocao;

    @NotNull(message = "A Data de Início da Promoção não pode ser vazia.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DTINIPROMO", nullable = false)
    private Date dataInicioPromocao;

    @NotNull(message = "A Data de Fim da Promoção não pode ser vazia.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DTFIMPROMO", nullable = false)
    private Date dataFimPromocao;

    @NotNull(message = "O Ativo da Promoção não pode ser vazio")
    @Column(name = "ATIVO", length = 1, nullable = false)
    private char ativo;

    @Column(name = "DESCVLRPROMO")
    private Double descVlrPromocao;

    @Column(name = "PERCVLRPROMO", precision = 4, scale = 2)
    private Double percVlrPromocao;

    public Promocao() {}

    public Promocao(int codigo, Livro livro, Genero genero, @NotNull(message = "A Descrição da Promoção não pode ser vazia.") @Length(max = 60, message = "A Descrição da Promoção deve conter até 60 dígitos.") String nomePromocao, @NotNull(message = "A Data de Início da Promoção não pode ser vazia.") Date dataInicioPromocao, @NotNull(message = "A Data de Fim da Promoção não pode ser vazia.") Date dataFimPromocao, @NotNull(message = "O Ativo da Promoção não pode ser vazio") char ativo, Double descVlrPromocao, Double percVlrPromocao) {
        this.codigo = codigo;
        this.livro = livro;
        this.genero = genero;
        this.nomePromocao = nomePromocao;
        this.dataInicioPromocao = dataInicioPromocao;
        this.dataFimPromocao = dataFimPromocao;
        this.ativo = ativo;
        this.descVlrPromocao = descVlrPromocao;
        this.percVlrPromocao = percVlrPromocao;
    }
}