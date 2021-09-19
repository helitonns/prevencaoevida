package br.leg.alrr.common.controller.acesso;

import br.leg.alrr.common.business.TipoUsuario;
import br.leg.alrr.common.model.acesso.Usuario;
import br.leg.alrr.common.persistence.acesso.UsuarioDAO;
import br.leg.alrr.common.util.Criptografia;
import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.common.util.FacesUtils;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

import javax.inject.Named;

/**
 *
 * @author heliton
 */
@Named
@SessionScoped
public class StartMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsuarioDAO usuarioDAO;

    private Usuario usuario;

    private String login = "";
    private String senha = "";
    private String senha1 = "";

    //===========================================================
    @PostConstruct
    private void init() {
        usuario = new Usuario();
    }

    public String logar() {
        try {
            usuario = usuarioDAO.pesquisarPorLogin(login);
            //ENCONTROU UM USUARIO COM O RESPECTIVO LOGIN
            if (usuario != null) {
                usuario = usuarioDAO.pesquisarPorLoginESenha(login, Criptografia.criptografarEmMD5(senha));
                if (usuario != null && usuario.isStatus()) {
                    FacesUtils.setBean("usuario", usuario);

                    if (usuario.getNome() == null || usuario.getMatricula() == null) {
                        FacesUtils.addWarnMessageFlashScoped("O usuário está com o nome ou matrícula não preenchidos. Complete o seu perfil!");
                        return "/pages/user/home.xhtml" + "?faces-redirect=true";
                    } else {
                        if (usuario.getTipo().equals(TipoUsuario.OUVINTE)) {
                            return "/pages/user/home.xhtml" + "?faces-redirect=true";
                        }else {
                            return "/pages/user/cadastro-ouvinte.xhtml" + "?faces-redirect=true";
                        }
                    }

                } else {
                    FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
                }
            } else {
                FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
        }
        return "/login.xhtml" + "?faces-redirect=true";
    }

    public String sair() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } catch (Exception e) {
        }
        return "/login.xhtml" + "?faces-redirect=true";
    }

    public void trocarSenha() {
        try {
            if (verificarForcaDaSenha(senha1)) {
                usuario.setSenha(Criptografia.criptografarEmMD5(senha1));
                usuarioDAO.atualizar(usuario);
                FacesUtils.addInfoMessage("Senha atualizada com sucesso!!!");
            } else {
                FacesUtils.addWarnMessage("A senha deve atender aos seguintes requisitos: ter no mínimo 8 caracteres, possuir letra minúcula 'a', "
                        + "possuir letra maiúscula 'A' e número '123'!!!");
            }

        } catch (DAOException e) {
            FacesUtils.addInfoMessage("Senha atualizada com sucesso!!!");
        }
    }

    public String salvarNomeMatricula() {

        try {
            usuarioDAO.atualizar(usuario);
            FacesUtils.addInfoMessage("Usuário atualizado com sucesso!!!");
            //Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método StartMB.salvarNomeMatricula().");
            return "home.xhtml" + "?faces-redirect=true";
        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro au atualizar senha.");
        }
        return null;
    }

    private boolean verificarForcaDaSenha(String senha) {
        if (senha.length() < 8) {
            return false;
        }

        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;

        for (char c : senha.toCharArray()) {
            if (c >= '0' && c <= '9') {
                achouNumero = true;
            } else if (c >= 'A' && c <= 'Z') {
                achouMaiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }
        }

        //defini os parâmetros que serão avalidados
        //neste caso não irá levar em consideração o requisito "símbolo"
        return achouNumero && achouMaiuscula && achouMinuscula;
    }

    //===========================================================
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isUsuarioRelatorio() {
        return usuario.getTipo().equals(TipoUsuario.RELATORIO);
    }

    public boolean isOperador() {
        return usuario.getTipo().equals(TipoUsuario.OPERADOR);
    }
    
    public boolean isOuvinte() {
        return usuario.getTipo().equals(TipoUsuario.OUVINTE);
    }

    public boolean isAdmin() {
        return usuario.getTipo().equals(TipoUsuario.ADMIN);
    }

    public boolean isSuperAdmin() {
        return usuario.getTipo().equals(TipoUsuario.SUPER_ADMIN);
    }

    public String getSenha1() {
        return senha1;
    }

    public void setSenha1(String senha1) {
        this.senha1 = senha1;
    }

}
