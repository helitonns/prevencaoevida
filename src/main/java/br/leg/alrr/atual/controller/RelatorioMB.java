package br.leg.alrr.atual.controller;

import br.leg.alrr.common.persistence.acesso.UsuarioDAO;
import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.common.util.FacesUtils;
import br.leg.alrr.common.util.Relatorio;
import br.leg.alrr.atual.model.Frequencia;
import br.leg.alrr.atual.model.Ouvinte;
import br.leg.alrr.atual.persistence.FrequenciaDAO;
import br.leg.alrr.atual.persistence.OuvinteDAO;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class RelatorioMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FrequenciaDAO frequenciaDAO;
    
    @EJB
    private UsuarioDAO usuarioDAO;

    @EJB
    private OuvinteDAO ouvinteDAO;

    private Frequencia frequencia;

    private List<Frequencia> frequencias;

    private Ouvinte ouvinte;

    private boolean removerOuvinte;
    
    private int parametroEscolhido;
    
    private boolean parametro;

    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

    }

    
    public String atualizarPresenca() {
        try {
            if (frequencia.getId() != null) {	
                frequencia.setPresenca1(!frequencia.isPresenca1());
                frequenciaDAO.atualizar(frequencia);
                if (frequencia.isPresenca1()) {
                	FacesUtils.addInfoMessageFlashScoped("Presença confirmada!");
                }else {
                	FacesUtils.addInfoMessageFlashScoped("Ausência confirmada!");
                }
                
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "relatorio.xhtml" + "?faces-redirect=true";
    }

    private void listarTodos() {
        try {
            frequencias = frequenciaDAO.listarTodos();
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao listar vídeos!");
        }
    }
    
    public void pesquisarTiposPresenca() {
        try {
        	
        	
        	switch (parametroEscolhido) {
            case 0:
            	frequencias = frequenciaDAO.listarTodos();
                break;
            case 1:
                parametro = true;
                frequencias = frequenciaDAO.listarTiposPresenca(parametro);
                break;
            case 2:
            	parametro = false;
            	frequencias = frequenciaDAO.listarTiposPresenca(parametro);
            	break;
        }
          
        	
        	
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao pesquisar os tipos de presença!");
        }
    }

    

    public String cancelarCadastro() {
        return "relatorio.xhtml" + "?faces-redirect=true";
    }

    /* Exportar Pdf */
    public void exportaPdf() {
        Relatorio<Frequencia> relatorio = new Relatorio<>();
        try {
            if ((frequencias.size() > 0)) {
                relatorio.exportarPdf(frequencias);;
            } else {
                FacesUtils.addInfoMessage("Não há registros!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            FacesUtils.addErrorMessage("Não há registros!");
        }
    }

    public String editarOuvinte() {
        FacesUtils.setBean("ouvinteEditar", ouvinte);
        return "cadastro-ouvinte.xhtml" + "?faces-redirect=true";
    }

    public String removerOuvinte() {
        try {
            if (removerOuvinte) {
                if (usuarioDAO.haUsuarioComEsteLogin(frequencia.getOuvinte().getEmail())) {
                    usuarioDAO.removerUusarioPorLogin(frequencia.getOuvinte().getEmail());
                }
                Ouvinte o = frequencia.getOuvinte();
                frequenciaDAO.remover(frequencia);
                ouvinteDAO.remover(o);
                FacesUtils.addInfoMessageFlashScoped("Usuário removido com sucesso!");
            }

        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao remover usuario!");
        }
        return "relatorio.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    private void iniciar() {
        removerOuvinte = false;
        ouvinte = new Ouvinte();
        frequencia = new Frequencia();
        pesquisarTiposPresenca();
    }

    //==========================================================================
    public List<Frequencia> getFrequencias() {
        return frequencias;
    }

    public Frequencia getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Frequencia frequencia) {
        this.frequencia = frequencia;
    }

    public Ouvinte getOuvinte() {
        return ouvinte;
    }

    public void setOuvinte(Ouvinte ouvinte) {
        this.ouvinte = ouvinte;
    }

    public FrequenciaDAO getFrequenciaDAO() {
        return frequenciaDAO;
    }

    public void setFrequenciaDAO(FrequenciaDAO frequenciaDAO) {
        this.frequenciaDAO = frequenciaDAO;
    }

    public boolean isRemoverOuvinte() {
        return removerOuvinte;
    }

    public void setRemoverOuvinte(boolean removerOuvinte) {
        this.removerOuvinte = removerOuvinte;
    }

	public int getParametroEscolhido() {
		return parametroEscolhido;
	}

	public void setParametroEscolhido(int parametroEscolhido) {
		this.parametroEscolhido = parametroEscolhido;
	}

	public boolean isParametro() {
		return parametro;
	}

	public void setParametro(boolean parametro) {
		this.parametro = parametro;
	}
    
    

}
