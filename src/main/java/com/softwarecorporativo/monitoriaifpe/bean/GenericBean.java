
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwarecorporativo.monitoriaifpe.bean;

import com.softwarecorporativo.monitoriaifpe.exception.MensagemExcecao;
import com.softwarecorporativo.monitoriaifpe.exception.NegocioException;
import com.softwarecorporativo.monitoriaifpe.modelo.negocio.EntidadeNegocio;
import com.softwarecorporativo.monitoriaifpe.servico.GenericService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.validation.ConstraintViolationException;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Edmilson Santana
 * @param <T>
 */
public abstract class GenericBean<T extends EntidadeNegocio> implements Serializable {

    private static final long serialVersionUID = 2056768767918963874L;

    protected T entidadeNegocio;

    protected GenericService<T> service;

    protected List<T> entidades = new ArrayList<>();

    @PostConstruct
    protected void inicializar() {
        inicializarEntidadeNegocio();
        inicializarServico();
    }

    public void mensagemAlteracaoSucesso() {
        this.adicionarMensagemView("Alteração realizada com sucesso!", FacesMessage.SEVERITY_INFO);
    }

    public void mensagemCadastroSucesso() {
        this.adicionarMensagemView("Cadastro realizado com sucesso!", FacesMessage.SEVERITY_INFO);
    }

    public void mensagemRemoverSucesso() {
        this.adicionarMensagemView("Cadastro removido com sucesso!", FacesMessage.SEVERITY_INFO);
    }

    public void adicionarMensagemView(String mensagem) {
        this.adicionarMensagemComponente(null, mensagem, null);
    }

    public void adicionarMensagemView(String mensagem, Severity severity) {
        this.adicionarMensagemComponente(null, mensagem, severity);
    }

    private void adicionarMensagemComponente(String componente, String mensagem, Severity severity) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(componente, new FacesMessage(severity, mensagem, null));
    }

    public T getEntidadeNegocio() {
        return entidadeNegocio;
    }

    public void setEntidadeNegocio(T entidadeNegocio) {
        this.entidadeNegocio = entidadeNegocio;
    }

    public void alterar() {
        try {
            this.service.atualizar(entidadeNegocio);
            mensagemAlteracaoSucesso();
        } catch (NegocioException ex) {
            adicionarMensagemView(ex.getMessage(), FacesMessage.SEVERITY_WARN);
        } catch (EJBException ejbe) {
            if (ejbe.getCause() instanceof ConstraintViolationException) {
                MensagemExcecao mensagemExcecao = new MensagemExcecao(ejbe.getCause());
                adicionarMensagemView(mensagemExcecao.getMensagem(), FacesMessage.SEVERITY_WARN);
            } else {
                throw ejbe;
            }
        }
    }

    public void cadastrar() {
        try {
            this.service.salvar(entidadeNegocio);
            mensagemCadastroSucesso();
            popularEntidades();
            inicializarEntidadeNegocio();
        } catch (NegocioException ex) {
            adicionarMensagemView(ex.getMessage(), FacesMessage.SEVERITY_WARN);
        } catch (EJBException ejbe) {
            if (ejbe.getCause() instanceof ConstraintViolationException) {
                MensagemExcecao mensagemExcecao = new MensagemExcecao(ejbe.getCause());
                adicionarMensagemView(mensagemExcecao.getMensagem(), FacesMessage.SEVERITY_WARN);
            } else {
                throw ejbe;
            }
        }

    }

    protected void popularEntidades() {
        entidades = this.service.listarTodos();
    }

    public List<T> getEntidades() {

        if (entidades.isEmpty()) {
            popularEntidades();
        }
        return entidades;
    }

    public void remover(T entidadeNegocio) {
        try {
            this.service.remover(entidadeNegocio);
            popularEntidades();
            mensagemRemoverSucesso();
        } catch (NegocioException ex) {
            adicionarMensagemView(ex.getMessage(), FacesMessage.SEVERITY_WARN);
        }

    }

    public GenericService<T> getService() {
        return service;
    }

    protected void setService(GenericService<T> service) {
        this.service = service;
    }

    public void alterarEntidadeCadastrada(RowEditEvent editEvent) {
        entidadeNegocio = (T) editEvent.getObject();
        alterar();
    }

    abstract void inicializarEntidadeNegocio();

    abstract void inicializarServico();

}
