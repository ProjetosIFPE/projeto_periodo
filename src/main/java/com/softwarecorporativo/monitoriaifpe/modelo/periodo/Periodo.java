package com.softwarecorporativo.monitoriaifpe.modelo.periodo;

import com.softwarecorporativo.monitoriaifpe.modelo.negocio.EntidadeNegocio;
import com.softwarecorporativo.monitoriaifpe.modelo.util.constantes.Semestre;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_PERIODO")
@AttributeOverrides({
    @AttributeOverride(name = "chavePrimaria", column = @Column(name = "PERIODO_ID"))})
@Access(AccessType.FIELD)
public class Periodo extends EntidadeNegocio {

    private static final long serialVersionUID = 21290824252510458L;

    @Min(value = 1970, message = "{com.softwarecorporativo.monitoriaifpe.periodo.ano}")
    @Column(name = "PERIODO_ANO", nullable = false)
    private Integer ano;

    @NotNull 
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PRIMEIRO', 'SEGUNDO')", name="SEMESTRE", nullable = false)
    private Semestre semestre;

    public Integer getAno() {

        return ano;
    }

    public void setAno(Integer ano) {

        this.ano = ano;
    }

    public Semestre getSemestre() {

        return semestre;
    }

    public void setSemestre(Semestre semestre) {

        this.semestre = semestre;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append(getAno());
        builder.append('.');
        builder.append(getSemestre().ordinal() + 1);
        return builder.toString();
    }
}
