package br.leg.alrr.atual.controller;

import br.leg.alrr.common.business.TipoUsuario;
import br.leg.alrr.common.model.acesso.Usuario;
import br.leg.alrr.common.util.FacesUtils;
import br.leg.alrr.atual.model.Frequencia;
import br.leg.alrr.atual.model.Ouvinte;
import br.leg.alrr.atual.model.Video;
import br.leg.alrr.atual.persistence.FrequenciaDAO;
import br.leg.alrr.atual.persistence.OuvinteDAO;
import br.leg.alrr.atual.persistence.VideoDAO;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class HomeMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private VideoDAO videoDAO;

    @EJB
    private OuvinteDAO ouvinteDAO;
    
    @EJB
    private FrequenciaDAO frequenciaDAO;

    private List<Video> videos;
    private Video video;
    private Ouvinte ouvinte;

    private String link;
    private String linkNormal;
    private boolean removerVideo;
    private boolean exibirVideo;
    private LocalDateTime dataEvento;
    private LocalDateTime dataFinalPresenca;

    //==========================================================================
    @PostConstruct
    public void init() {
        link = montarLink();
        iniciar();
    }

    public String salvarVideo() {
        try {
            videoDAO.salvar(video);
            FacesUtils.addInfoMessageFlashScoped("Vídeo salvo com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao salvar vídeo!");
        }
        return "video.xhtml" + "?faces-redirect=true";
    }

    public void listarTodos() {
        try {
            videos = videoDAO.listarTodos();
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao listar vídeos!");
        }
    }

    public String removerVideo() {
        try {
            videoDAO.remover(video);
            FacesUtils.addInfoMessageFlashScoped("Vídeo removido com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao remover vídeo!");
        }
        return "video.xhtml" + "?faces-redirect=true";
    }

    public String cancelarCadastro() {
        return "video.xhtml" + "?faces-redirect=true";
    }

    public void confirmarPresenca1() {
        
        Usuario u = (Usuario) FacesUtils.getBean("usuario");
        
        if (u.getTipo().equals(TipoUsuario.OUVINTE)) {
            LocalDateTime ldt = LocalDateTime.now();
            
            boolean igualOuMaior = ldt.isEqual(dataEvento) || ldt.isAfter(dataEvento);
            boolean igualOuMenor = ldt.isEqual(dataFinalPresenca) || ldt.isBefore(dataFinalPresenca);
            
            if (igualOuMaior && igualOuMenor) {
                try {
                    Frequencia f = frequenciaDAO.buscarPorOuvinte(ouvinte);
                    f.setPresenca1(true);
                    frequenciaDAO.atualizar(f);
                    FacesUtils.addInfoMessage("Presença salva com sucesso!");
                } catch (Exception e) {
                    System.out.println(e.getCause().toString());
                }
                exibirVideo = true;
            }else{
                FacesUtils.addWarnMessage("A presença deve ser confirmada entre "
                        + dataEvento.getHour() +":"
                        + dataEvento.getMinute()+" do dia "
                        + dataEvento.getDayOfMonth()+"/"+dataEvento.getMonthValue()+" e "
                        + dataFinalPresenca.getHour() +":"
                        + dataFinalPresenca.getMinute()+" do dia "
                        + dataFinalPresenca.getDayOfMonth()+"/"+dataFinalPresenca.getMonthValue());
            }
        } else {
            FacesUtils.addWarnMessage("Sem ouvinte na sessão!");
        }
        
    }
    
    public boolean verificarSeOuvinteConfirmouPresenca(){
        try {
            return frequenciaDAO.verificarSeOuvinteConfirmouPresenca(ouvinte);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
        }
        return false;
    }

    //==========================================================================
    private void iniciar() {
        videos = new ArrayList<>();

        video = new Video();
        ouvinte = new Ouvinte();

        removerVideo = false;
        exibirVideo = false;

        listarTodos();
        buscarOuvinte();
    }

    private String montarLink() {
        try {
            Video v = videoDAO.listarOUltimoRegistro();
            dataEvento = v.getDataEvento();
            dataFinalPresenca = v.getDataFinalPresenca();
            
            linkNormal = v.getSrc();
            
            return "<input id=\"srcVideo\" type=\"text\" value=\"" + v.getSrc2() + "\" hidden=\"true\"/>";
        } catch (Exception e) {
            return "";
        }
    }

    private void buscarOuvinte() {
        try {
            Usuario u = (Usuario) FacesUtils.getBean("usuario");

            if (u.getTipo().equals(TipoUsuario.OUVINTE)) {
                ouvinte = ouvinteDAO.buscarPorEmail(u.getLogin());
            }
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
        }
    }

    //==========================================================================
    public String getLink() {
        return link;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public boolean isRemoverVideo() {
        return removerVideo;
    }

    public void setRemoverVideo(boolean removerVideo) {
        this.removerVideo = removerVideo;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public boolean isExibirVideo() {
        return exibirVideo;
    }

    public LocalDateTime getDataEvento() {
        return dataEvento;
    }

    public LocalDateTime getDataFinalPresenca() {
        return dataFinalPresenca;
    }

    public String getLinkNormal() {
        return linkNormal;
    }
    
    
}
