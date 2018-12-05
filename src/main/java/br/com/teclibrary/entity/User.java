package br.com.teclibrary.entity;

import br.com.teclibrary.DAO.RoleDAO;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "User")
@Table(name = "TUSER")
@NamedQuery(name = "User.findByLogin",
        query = "SELECT U FROM User U WHERE UPPER(U.login) = :P_LOGIN")
@Check(constraints = "ATIVO IN ('S', 'N')")
@Data
@Builder
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(generator = "SEQ_TUSER", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_TUSER", sequenceName = "SEQ_TUSER", allocationSize = 1)
    @Column(name = "CODUSU", nullable = false)
    private int codigo;

    @Length(max = 80, message = "O Nome do Usuário não pode ter mais do que 100 caracteres.")
    @NotNull(message = "O Nome do usuário não pode ser vazio.")
    @Column(name = "NOMEUSU", nullable = false, length = 100)
    private String nome;

    @Length(max = 30, message = "O Login do usuário não pode ter mais do que 30 caracteres.")
    @NotNull(message = "O Login do usuário não pode ser vazio.")
    @Column(name = "LOGIN", nullable = false, unique = true, length = 30)
    private String login;

    @Length(max = 100, message = "A Senha do usuário não pode ter mais do que 100 caracteres.")
    @NotNull(message = "A Senha do usuário não pode ser vazia.")
    @Column(name = "SENHA", nullable = false, length = 100)
    private String senha;

    @Email(message = "O E-mail do usuário está em formato inválido.")
    @Length(max = 80, message = "O E-mail do usuário não pode ter mais do que 100 caracteres.")
    @NotNull(message = "O E-mail do usuário não pode ser vazio.")
    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @NotNull(message = "O CPF do Usuário não pode ser vazio.")
    @Column(name = "CPF", length = 11, nullable = false)
    private String cpf;

    @NotNull(message = "O Telefone do Usuário não pode ser vazio.")
    @Column(name = "TELEFONE", length = 11, nullable = false)
    private String telefone;

    @Length(max = 40, message = "A Profissão do usuário não pode ter mais do que 40 caracteres.")
    @NotNull(message = "A Profissão do usuário não pode ser vazia.")
    @Column(name = "PROFISSAO", nullable = false, length = 40)
    private String profissao;

    @NotNull(message = "O Ativo de usuário não pode ser vazio.")
    @Column(name = "ATIVO", nullable = false)
    private char ativo = 'S';

    @ManyToOne(targetEntity = Role.class)
    @JoinColumn(name = "CODROLE")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Role role;

    @NotNull(message = "Data/Hora do Cadastro do usuário não pode ser vazio.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DHCADASTRO", nullable = false)
    private Date dhCadastro = new Date();

    @NotNull(message = "Data/Hora da Última alteração do usuário não pode ser vazia.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DHULTALTER", nullable = false)
    private Date dhUltAlteracao = new Date();

    @Length(max = 80, message = "A Observação do Usuário deve conter até 11 dígitos.")
    @Column(name = "OBSERVACAO", length = 80)
    private String observacao;

    public User() {}

    public User(int codigo, @Length(max = 80, message = "O Nome do Usuário não pode ter mais do que 100 caracteres.") @NotNull(message = "O Nome do usuário não pode ser vazio.") String nome, @Length(max = 30, message = "O Login do usuário não pode ter mais do que 30 caracteres.") @NotNull(message = "O Login do usuário não pode ser vazio.") String login, @Length(max = 100, message = "A Senha do usuário não pode ter mais do que 100 caracteres.") @NotNull(message = "A Senha do usuário não pode ser vazia.") String senha, @Email(message = "O E-mail do usuário está em formato inválido.") @Length(max = 80, message = "O E-mail do usuário não pode ter mais do que 100 caracteres.") @NotNull(message = "O E-mail do usuário não pode ser vazio.") String email, @NotNull(message = "O CPF do Usuário não pode ser vazio.") String cpf, @NotNull(message = "O Telefone do Usuário não pode ser vazio.") String telefone, @Length(max = 40, message = "A Profissão do usuário não pode ter mais do que 40 caracteres.") @NotNull(message = "A Profissão do usuário não pode ser vazia.") String profissao, @NotNull(message = "O Ativo de usuário não pode ser vazio.") char ativo, Role role, @NotNull(message = "Data/Hora do Cadastro do usuário não pode ser vazio.") Date dhCadastro, @NotNull(message = "Data/Hora da Última alteração do usuário não pode ser vazia.") Date dhUltAlteracao, @Length(max = 80, message = "A Observação do Usuário deve conter até 11 dígitos.") String observacao) {
        this.codigo = codigo;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.profissao = profissao;
        this.ativo = ativo;
        this.role = role;
        this.dhCadastro = dhCadastro;
        this.dhUltAlteracao = dhUltAlteracao;
        this.observacao = observacao;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = RoleDAO.retrieveUserRoles(this.getRole());
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.getSenha();
    }

    @Override
    public String getUsername() {
        return this.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
