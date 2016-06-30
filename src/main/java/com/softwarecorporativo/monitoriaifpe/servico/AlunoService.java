/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwarecorporativo.monitoriaifpe.servico;

import com.softwarecorporativo.monitoriaifpe.modelo.grupo.Grupo;
import com.softwarecorporativo.monitoriaifpe.modelo.aluno.Aluno;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ejb.EJB;

/**
 *
 * @author Douglas Albuqerque
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AlunoService extends GenericService<Aluno> {

    @EJB
    private GrupoService grupoService;

    @Override
    public Aluno salvar(Aluno entidadeNegocio) {
        entidadeNegocio.adicionarGrupo(grupoService.obterGrupo(Grupo.USUARIO));
        entidadeNegocio.adicionarGrupo(grupoService.obterGrupo(Grupo.ALUNO));
        return super.salvar(entidadeNegocio);
    }

    @Override
    public Class<Aluno> getClasseEntidade() {
        return Aluno.class;
    }

    @Override
    public Aluno getEntidadeNegocio() {
        return new Aluno();
    }

}
