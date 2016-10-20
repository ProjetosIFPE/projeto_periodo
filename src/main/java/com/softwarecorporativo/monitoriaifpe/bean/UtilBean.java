/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwarecorporativo.monitoriaifpe.bean;

import com.softwarecorporativo.monitoriaifpe.modelo.util.constantes.Grau;
import com.softwarecorporativo.monitoriaifpe.modelo.util.constantes.Semestre;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Edmilson Santana
 */
@ManagedBean
@ApplicationScoped
public class UtilBean implements Serializable {

    private static final long serialVersionUID = -7393215500241022802L;

    public Grau[] getGraus() {
        return Grau.values();
    }

    public Semestre[] getSemestres() {
        return Semestre.values();
    }

}
