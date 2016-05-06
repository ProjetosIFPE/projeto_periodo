package com.softwarecorporativo.monitoriaifpe.disciplina;

import com.softwarecorporativo.monitoriaifpe.curso.Curso;
import com.softwarecorporativo.monitoriaifpe.professor.Professor;
import com.softwarecorporativo.monitoriaifpe.aluno.Aluno;
import com.softwarecorporativo.monitoriaifpe.negocio.EntidadeNegocio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TB_COMPONENTE_CURRICULAR")
@AttributeOverrides({
    @AttributeOverride(name = "chavePrimaria", column = @Column(name = "COMP_CURRICULAR_ID"))})
@Access(AccessType.FIELD)
public class ComponenteCurricular extends EntidadeNegocio {

    @NotBlank
    @Column(name = "CODIGO_COMP_CURRICULAR", nullable = false, unique = true)
    private String codigoComponenteCurricular;

    @NotBlank
    @Size(min = 1, max = 150)
    @Pattern(regexp = "^[A-Z]{1}\\D+$", message = "{com.softwarecorporativo.monitoriaifpe.componenteCurricular.descricao}")
    @Column(name = "COMP_CURRICULAR_DS", nullable = false)
    private String descricao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURSO_ID", referencedColumnName = "CURSO_ID")
    private Curso curso;

    public String getDescricao() {

        return descricao;
    }

    public void setDescricao(String descricao) {

        this.descricao = descricao;
    }

    public Curso getCurso() {

        return curso;
    }

    public void setCurso(Curso curso) {

        this.curso = curso;
    }

    public String getCodigoComponenteCurricular() {
        return codigoComponenteCurricular;
    }

    public void setCodigoComponenteCurricular(String codigoComponenteCurricular) {
        this.codigoComponenteCurricular = codigoComponenteCurricular;
    }
}