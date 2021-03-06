package com.softwarecorporativo.monitoriaifpe.modelo.monitoria;

import com.softwarecorporativo.monitoriaifpe.modelo.aluno.Aluno;
import com.softwarecorporativo.monitoriaifpe.modelo.atividade.Atividade;
import com.softwarecorporativo.monitoriaifpe.modelo.disciplina.Disciplina;
import com.softwarecorporativo.monitoriaifpe.modelo.monitoria.validation.ValidaMonitoria;
import com.softwarecorporativo.monitoriaifpe.modelo.negocio.EntidadeNegocio;
import com.softwarecorporativo.monitoriaifpe.modelo.periodo.Periodo;
import com.softwarecorporativo.monitoriaifpe.modelo.util.constantes.Modalidade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_MONITORIA")
@AttributeOverrides({
    @AttributeOverride(name = "chavePrimaria", column = @Column(name = "MONITORIA_ID"))})
@Access(AccessType.FIELD)
@ValidaMonitoria
public class Monitoria extends EntidadeNegocio {

    private static final long serialVersionUID = -4572493586452867519L;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidade modalidade;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "DISCIPLINA_ID", referencedColumnName = "DISCIPLINA_ID")
    private Disciplina disciplina;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ALUNO_ID", referencedColumnName = "ALUNO_ID")
    private Aluno aluno;

    @OneToMany(mappedBy = "monitoria", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atividade> atividades;

    public int getAnoMonitoria() {
        return disciplina.obterAnoDaDisciplina();
    }

    public String getEditalMonitoria() {
        return disciplina.getPeriodo().toString();
    }

    public Modalidade getModalidade() {

        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {

        this.modalidade = modalidade;
    }

    public Disciplina getDisciplina() {

        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {

        this.disciplina = disciplina;
    }

    public Atividade getAtividade(int index) {
        if (this.atividades == null) {
            this.atividades = new ArrayList<>();
        }
        return atividades.get(index);
    }

    public void addAtividade(Atividade atividade) {
        if (this.atividades == null) {
            this.atividades = new ArrayList<>();
        }
        atividade.setMonitoria(this);
        this.atividades.add(atividade);
    }

    public void setAluno(Aluno aluno) {

        this.aluno = aluno;

    }

    public Aluno getAluno() {

        return aluno;
    }

    public String getNomeMonitor() {
        return aluno.getNomeCompleto();
    }

    public String getNomeOrientador() {
        return disciplina.getProfessor().getNomeCompleto();
    }

    public Periodo getPeriodoMonitoria() {
        return disciplina.getPeriodo();
    }

}
