package br.leg.alrr.atual.controller;

import br.leg.alrr.common.util.FacesUtils;
import br.leg.alrr.atual.model.Evento;
import br.leg.alrr.atual.persistence.EventoDAO;
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
public class EventoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EventoDAO eventoDAO;
    
    private List<Evento> eventos;
    
    private Evento evento;
    
    private boolean removerEvento;
       
    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
    }

    public String salvar() {
        try {
            eventoDAO.salvar(evento);
            FacesUtils.addInfoMessageFlashScoped("Evento salva com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao salvar entidade!");
        }
        return "evento.xhtml" + "?faces-redirect=true";
    }

    public void listarTodos() {
        try {
            eventos = eventoDAO.listarTodos();
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao listar eventos!");
        }
    }
    
    public String remover() {
        try {
            eventoDAO.remover(evento);
            FacesUtils.addInfoMessageFlashScoped("Evento removida com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao remover entidade!");
        }
        return "evento.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "evento.xhtml" + "?faces-redirect=true";
    }
    //==========================================================================
    private void iniciar() {
        eventos = new ArrayList<>();
        evento = new Evento();
        removerEvento = false;
        listarTodos();
    }
   
    //==========================================================================

    public List<Evento> getEventos() {
        return eventos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public boolean isRemoverEvento() {
        return removerEvento;
    }

    public void setRemoverEvento(boolean removerEvento) {
        this.removerEvento = removerEvento;
    }
}
