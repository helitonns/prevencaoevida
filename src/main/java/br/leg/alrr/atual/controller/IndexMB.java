package br.leg.alrr.atual.controller;

import br.leg.alrr.common.business.Endereco;
import br.leg.alrr.common.business.ServicoDeCep;
import br.leg.alrr.common.business.TipoUsuario;
import br.leg.alrr.common.model.acesso.Usuario;
import br.leg.alrr.atual.model.Ouvinte;
import br.leg.alrr.common.persistence.acesso.UsuarioDAO;
import br.leg.alrr.atual.persistence.OuvinteDAO;
import br.leg.alrr.common.util.Criptografia;
import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.common.util.EmailUtils;
import br.leg.alrr.common.util.FacesUtils;
import br.leg.alrr.common.util.Mensagem;
import br.leg.alrr.common.util.PasswordGenerator;
import br.leg.alrr.atual.model.Evento;
import br.leg.alrr.atual.model.Frequencia;
import br.leg.alrr.atual.model.Video;
import br.leg.alrr.atual.persistence.EventoDAO;
import br.leg.alrr.atual.persistence.FrequenciaDAO;
import br.leg.alrr.atual.persistence.VideoDAO;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.mail.EmailException;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class IndexMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private OuvinteDAO ouvinteDAO;

    @EJB
    private UsuarioDAO usuarioDAO;

    @EJB
    private FrequenciaDAO frequenciaDAO;

    @EJB
    private EventoDAO eventoDAO;

    @EJB
    private VideoDAO videoDAO;

    private Ouvinte ouvinte;
    private Usuario usuario;
    private Evento evento;
    private Mensagem mensagem;

    private String senha1;
    private String senha2;
    private String login;
    private String novaSenha;
    private boolean exibirFormulario = false;
    private boolean habilitarInstituicao = false;
    private Long quantidadeDeOuvintesPresenciais = 0l;

    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

        try {
            Ouvinte o = (Ouvinte) FacesUtils.getBean("ouvinteEditar");
            if (o.getId() != null) {
                ouvinte = o;
                FacesUtils.removeBean("ouvinteEditar");
            }
        } catch (Exception e) {
        }
    }

    public String salvarOuvinte() {
        try {
            if ((quantidadeDeOuvintesPresenciais + 1) > evento.getNumeroDeVagas() && ouvinte.isPresencial()) {
                FacesUtils.addWarnMessageFlashScoped("O número de vagas presenciais já foram preenchidas. Selecione a opção para assistir ao evento REMOTAMENTE");
                return "";
            } else {
                if (ouvinteDAO.haOvinteComCPF(ouvinte.getCpf())) {
                    FacesUtils.addWarnMessageFlashScoped("Já há um usuário cadastrado com esse CPF");
                    return "";
                } else {

                    if (!usuarioDAO.haOLogin(ouvinte.getEmail())) {

                        usuario.setNome(ouvinte.getNome().toUpperCase());
                        usuario.setSenha(Criptografia.criptografarEmMD5(senha1));
                        usuario.setStatus(true);
                        usuario.setMatricula("0000");
                        usuario.setTipo(TipoUsuario.OUVINTE);
                        usuario.setLogin(ouvinte.getEmail().trim());

                        ouvinteDAO.salvar(ouvinte);
                        usuarioDAO.salvar(usuario);

                        Frequencia f = new Frequencia();
                        f.setOuvinte(ouvinte);
                        f.setPresenca1(false);
                        f.setPresenca2(false);

                        frequenciaDAO.salvar(f);

                        FacesUtils.addInfoMessageFlashScoped("Cadastro salvo com sucesso!!!");
                        FacesUtils.addInfoMessageFlashScoped("");
                        Video v = videoDAO.listarOUltimoRegistro();
                        int dia = v.getDataEvento().getDayOfMonth();
                        int mes = v.getDataEvento().getMonthValue();
                        int ano = v.getDataEvento().getYear();
                        String data = dia +"/"+ mes +"/"+ ano;
                        if (ouvinte.isPresencial()) {
                            FacesUtils.addInfoMessageFlashScoped("Na data "+ data +" compareça ao plenarinho para assistir ao evento!");
                        } else {
                            FacesUtils.addInfoMessageFlashScoped("Na data "+ data +" acesse o sistema clicando no botão \"Entrar\", no fim da página, para acompanhar o evento remotamente!");
                        }
                    } else {
                        FacesUtils.addWarnMessageFlashScoped("Já há um usuário cadastrado com esse e-mail, forneça outro!!!");
                        return "";
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Houve um erro ao salvar o cadastro!");
        }
        return "index.xhtml" + "?faces-redirect=true";
    }

    public String cadastrarOuvinte() {
        try {
            if ((quantidadeDeOuvintesPresenciais + 1) > evento.getNumeroDeVagas() && ouvinte.isPresencial()) {
                FacesUtils.addWarnMessageFlashScoped("O número de vagas presenciais já foram preenchidas. Selecione a opção para assistir ao evento REMOTAMENTE");
                return "";
            } else {
                if (ouvinteDAO.haOvinteComCPF(ouvinte.getCpf()) && ouvinte.getId() == null) {
                    FacesUtils.addWarnMessageFlashScoped("Já há um usuário cadastrado com esse CPF");
                    return "";
                } else {
                    if (ouvinte.getId() != null) {
                        ouvinteDAO.atualizar(ouvinte);
                        FacesUtils.addInfoMessageFlashScoped("Cadastro atualizado com sucesso!!!");
                    } else {

                        ouvinteDAO.salvar(ouvinte);

                        Frequencia f = new Frequencia();
                        f.setOuvinte(ouvinte);
                        f.setPresenca1(true);
                        f.setPresenca2(true);

                        frequenciaDAO.salvar(f);

                        FacesUtils.addInfoMessageFlashScoped("Cadastro salvo com sucesso!!!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
//            FacesUtils.addErrorMessageFlashScoped("Houve um erro ao salvar o cadastro!");
        }
        return "cadastro-ouvinte.xhtml" + "?faces-redirect=true";
    }

    public void preencherLogin(ValueChangeEvent evet) {
        usuario.setLogin(evet.getNewValue().toString());
    }

    public void habilitarInstituicaoListener(ValueChangeEvent evet) {
        String s = (String) evet.getNewValue();
        habilitarInstituicao = !s.equals("NÃO");
    }

    public void buscarEnderecoListener(ValueChangeEvent evet) {
        try {

            String cep = (String) evet.getNewValue();
            if (cep.length() == 8) {
                Endereco e = ServicoDeCep.buscaEnderecoPelo(cep);
                ouvinte.setBairro(e.getBairro());
                ouvinte.setLogradouro(e.getLogradouro());
            }
        } catch (Exception e) {
        }

    }

    public String cancelar() {
        return "index.xhtml" + "?faces-redirect=true";
    }

    public String cancelarCadastro() {
        return "cadastro-ouvinte.xhtml" + "?faces-redirect=true";
    }

    public String irParaLogin() {
        return "login-ouvinte.xhtml" + "?faces-redirect=true";
    }

    public void exibirFormulario() {
        exibirFormulario = true;
    }

    public String recuperarSenha() {
        try {
            usuario = usuarioDAO.pesquisarPorLogin(login);
            //ENCONTROU UM USUARIO COM O RESPECTIVO LOGIN/EMAIL
            if (usuario != null) {
                PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                        .useDigits(true)
                        .useLower(true)
                        .useUpper(true)
                        .build();
                novaSenha = passwordGenerator.generate(8);
                System.out.println("Teste: " + novaSenha);
                mensagem.setDestino(login);
                mensagem.setTitulo("Recuperar senha");
                mensagem.setMensagem("Sua nova senha é: " + novaSenha);
                trocarSenha(novaSenha);
                enviaEmail(mensagem);
                FacesUtils.addInfoMessageFlashScoped("Nova senha enviada com sucesso para: " + login + ", verifique seu e-mail!!!");
            }
            return "recuperar-senha.xhtml" + "?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Houve um erro ao enviar o e-mail: usuário não encontrado, verifique se colocou o e-mail correto!");
        }
        return "recuperar-senha" + "?faces-redirect=true";
    }

    public void trocarSenha(String newPassword) {
        try {
            usuario.setSenha(Criptografia.criptografarEmMD5(newPassword));
            usuarioDAO.atualizar(usuario);
        } catch (DAOException e) {
            System.out.println("LOG: " + e.getCause().toString());

        }
    }

    public void enviaEmail(Mensagem msg) throws DAOException {
        try {
            EmailUtils.enviaEmail(msg);

        } catch (EmailException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro! Occoreu um erro ao enviar a mensagem.", "Erro"));
            Logger.getLogger(IndexMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //==========================================================================
    private void iniciar() {
        ouvinte = new Ouvinte();
        ouvinte.setPresencial(false);
        usuario = new Usuario();
        evento = new Evento();
        mensagem = new Mensagem();

        senha1 = "";
        senha2 = "";
        login = "";
        novaSenha = "";
        exibirFormulario = false;
        habilitarInstituicao = false;

        buscarUltimoEvento();
        buscarQuantidadeDeOuvintesPresenciais();
    }

    private void buscarUltimoEvento() {
        try {
            evento = eventoDAO.listarOUltimoRegistro();
        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao buscar último evento!");
        }
    }

    private void buscarQuantidadeDeOuvintesPresenciais() {
        try {
            quantidadeDeOuvintesPresenciais = ouvinteDAO.quantidadePresencial();
        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Erro ao buscar último evento!");
        }
    }

    //==========================================================================
    public boolean isExibirFormulario() {
        return exibirFormulario;
    }

    public Ouvinte getOuvinte() {
        return ouvinte;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getSenha1() {
        return senha1;
    }

    public void setSenha1(String senha1) {
        this.senha1 = senha1;
    }

    public String getSenha2() {
        return senha2;
    }

    public void setSenha2(String senha2) {
        this.senha2 = senha2;
    }

    public boolean isHabilitarInstituicao() {
        return habilitarInstituicao;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

}
