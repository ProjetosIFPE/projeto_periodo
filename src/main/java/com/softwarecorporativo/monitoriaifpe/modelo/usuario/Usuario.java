package com.softwarecorporativo.monitoriaifpe.modelo.usuario;

import com.softwarecorporativo.monitoriaifpe.modelo.grupo.Grupo;
import com.softwarecorporativo.monitoriaifpe.modelo.negocio.EntidadeNegocio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TB_USUARIO")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "PAPEL_USUARIO", discriminatorType = DiscriminatorType.STRING, length = 1)
@AttributeOverrides({
    @AttributeOverride(name = "chavePrimaria", column = @Column(name = "USUARIO_ID"))})
@Access(AccessType.FIELD)
public class Usuario extends EntidadeNegocio {

    private static final long serialVersionUID = -2083194086477441520L;

    @NotBlank
    @Size(min = 1, max = 30)
    @Pattern(regexp = "^[A-Z]{1}[A-Za-z]+$", message = "{com.softwarecorporativo.monitoriaifpe.usuario.nome}")
    @Column(name = "USUARIO_NOME", nullable = false)
    private String nome;

    @NotBlank
    @Size(min = 1, max = 30)
    @Pattern(regexp = "^[A-Z]{1}[A-Za-z]+$", message = "{com.softwarecorporativo.monitoriaifpe.usuario.sobrenome}")
    @Column(name = "USUARIO_SOBRENOME", nullable = false)
    private String sobrenome;

    @NotBlank
    @Size(min = 3, max = 16)
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "{com.softwarecorporativo.monitoriaifpe.usuario.login}")
    @Column(name = "USUARIO_LOGIN", nullable = false)
    private String login;

    /* Adicionar caracteres especiais */
    @NotBlank
    @Size(min = 6, max = 18)
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "{com.softwarecorporativo.monitoriaifpe.usuario.senha}")
    @Column(name = "USUARIO_SENHA", nullable = false)
    private String senha;

    @NotBlank
    @Size(min = 1, max = 30)
    @Email
    @Column(name = "USUARIO_EMAIL", length = 30, nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_USUARIO_GRUPO", joinColumns = @JoinColumn(name = "USUARIO_ID"), inverseJoinColumns = @JoinColumn(name = "GRUPO_ID"))
    private List<Grupo> grupos;

    public String getLogin() {

        return login;
    }

    public String getSenha() {

        return senha;
    }

    public void setLogin(String login) {

        this.login = login;
    }

    public void setSenha(String senha) {

        this.senha = senha;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public String getNome() {

        return nome;
    }

    public String getSobrenome() {
        return this.sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getNomeCompleto() {
        return String.format("%s %s", nome, sobrenome);
    }

    public void adicionarGrupo(Grupo grupo) {
        if (grupos == null) {
            this.grupos = new ArrayList<>();
        }
        grupos.add(grupo);
    }
}
