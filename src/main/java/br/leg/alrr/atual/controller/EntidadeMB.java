package br.leg.alrr.atual.controller;

import br.leg.alrr.common.util.FacesUtils;
import br.leg.alrr.atual.model.Entidade;
import br.leg.alrr.atual.persistence.EntidadeDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Heliton
 */
@Named
@ViewScoped
public class EntidadeMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EntidadeDAO entidadeDAO;
    
    private List<Entidade> entidades;
    
    private Entidade entidade;
    
    private boolean removerEntidade;
       
    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
    }

    public String salvar() {
        try {
            entidadeDAO.salvar(entidade);
            FacesUtils.addInfoMessageFlashScoped("Entidade salva com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao salvar entidade!");
        }
        return "entidade.xhtml" + "?faces-redirect=true";
    }

    public void listarTodos() {
        try {
            entidades = entidadeDAO.listarTodos();
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao listar entidades!");
        }
    }
    
    public String remover() {
        try {
            entidadeDAO.remover(entidade);
            FacesUtils.addInfoMessageFlashScoped("Entidade removida com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao remover entidade!");
        }
        return "entidade.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "entidade.xhtml" + "?faces-redirect=true";
    }
    //==========================================================================
    private void iniciar() {
        entidades = new ArrayList<>();
        entidade = new Entidade();
        removerEntidade = false;
        listarTodos();
    }
   
    //==========================================================================

    public List<Entidade> getEntidades() {
        return entidades;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public boolean isRemoverEntidade() {
        return removerEntidade;
    }

    public void setRemoverEntidade(boolean removerEntidade) {
        this.removerEntidade = removerEntidade;
    }
}
