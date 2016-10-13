/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwarecorporativo.monitoriaifpe.servico;

import com.softwarecorporativo.monitoriaifpe.exception.NegocioException;
import com.softwarecorporativo.monitoriaifpe.modelo.grupo.Grupo;
import com.softwarecorporativo.monitoriaifpe.modelo.usuario.Usuario;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Edmilson Santana
 * @param <T>
 */
public abstract class UsuarioService<T extends Usuario> extends GenericService<T> {

    private GrupoService servicoGrupo;

    private EmailService emailService;

    @PostConstruct
    public void inicializarServicos() {
        servicoGrupo = inicializarServicoGrupo();
        emailService = inicializarServicoEmail();
    }

    @Override
    public T salvar(T usuario) throws NegocioException {
        adicionarGrupoUsuario(usuario);
        usuario = super.salvar(usuario);
       // emailService.enviarMensagem(usuario.getEmail());
        return usuario;

    }

    protected void adicionarGrupoUsuario(Usuario usuario) {
        usuario.adicionarGrupo(servicoGrupo.obterGrupo(Grupo.USUARIO));
        this.adicionarGrupos(usuario);
    }

    @Deprecated
    public Usuario getUsuarioPorLogin(String login) {
        TypedQuery<T> query = super.entityManager
                .createNamedQuery(T.USUARIO_CADASTRADO, this.getClasseEntidade());
        query.setParameter(1, login);
        return query.getSingleResult();
    }

    @Override
    public Boolean verificarExistencia(T entidadeNegocio) {
        String[] atributos = new String[2];
        atributos[0] = entidadeNegocio.getEmail();
        atributos[1] = entidadeNegocio.getCpf();
        return count(Usuario.COUNT_USUARIO_CADASTRADO, atributos) > 0;
    }

    @Override
    public Class<T> getClasseEntidade() {
        return (Class<T>) Usuario.class;
    }

    abstract void adicionarGrupos(Usuario usuario);

    abstract GrupoService inicializarServicoGrupo();

    abstract EmailService inicializarServicoEmail();

}
