package com.softwarecorporativo.monitoriaifpe.aluno;

import com.softwarecorporativo.monitoriaifpe.aluno.validation.ValidaMatricula;
import com.softwarecorporativo.monitoriaifpe.curso.Curso;
import com.softwarecorporativo.monitoriaifpe.disciplina.Disciplina;
import com.softwarecorporativo.monitoriaifpe.usuario.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TB_ALUNO")
@PrimaryKeyJoinColumn(name = "ALUNO_ID")
@DiscriminatorValue(value = "A")
@Access(AccessType.FIELD)
@ValidaMatricula
public class Aluno extends Usuario {

    @NotBlank
    @Column(name = "ALUNO_MATRICULA", nullable = false)
    private String matricula;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURSO_ID", referencedColumnName = "CURSO_ID")
    private Curso curso;

    @ManyToMany(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
    @JoinTable(name = "TB_DISCIPLINA_ALUNO",
            joinColumns = @JoinColumn(name = "ALUNO_ID"),
            inverseJoinColumns = @JoinColumn(name = "DISCIPLINA_ID"))
    private List<Disciplina> disciplinas;

    public String getMatricula() {

        return matricula;
    }

    public void setMatricula(String matricula) {

        this.matricula = matricula;
    }

    public Curso getCurso() {

        return curso;
    }

    public void setCurso(Curso curso) {

        this.curso = curso;
    }

    public Boolean verificarDisciplinaNoAluno(Disciplina disciplina) {
        Boolean possuiDisciplina = Boolean.FALSE;
        if (this.disciplinas != null) {
            possuiDisciplina = this.disciplinas.contains(disciplina);
        }
        return possuiDisciplina;
    }

    public void addDisciplina(Disciplina disciplina) {
        if (this.disciplinas == null) {
            this.disciplinas = new ArrayList<>();
        }
        this.disciplinas.add(disciplina);
    }

    public int quantidadeDisciplinasCursadas() {

        return disciplinas.size();
    }
}