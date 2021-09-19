package br.leg.alrr.common.persistence.acesso;

import br.leg.alrr.common.model.acesso.Usuario;
import br.leg.alrr.common.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade Usuario.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Usuario
 */
@Stateless
public class UsuarioDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Usuario o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar usuário.", e);
        }
    }

    public void atualizar(Usuario o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar usuário.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Usuario o order by o.login asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public List listarTodosSemSuperAdmin() throws DAOException{
        try {
            return em.createQuery("select o from Usuario o where o.tipo <> :tipo order by o.login asc")
                    .setParameter("tipo", "SUPER_ADMIN")
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public List listarTodosAtivos() throws DAOException{
        try {
            return em.createQuery("select o from Usuario o where o.status = true order by o.login asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public void remover(Usuario o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover usuário.", e);
        }
    }

    public Usuario pesquisarPorLogin(String login) throws DAOException{
        try {
            return (Usuario) em.createQuery("select u from Usuario u where u.login =:login")
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login.", e);
        }
    }
    
    public boolean haOLogin(String login) throws DAOException{
        try {
             List<Usuario> u =  em.createQuery("select u from Usuario u where u.login =:login")
                    .setParameter("login", login)
                    .getResultList();
             
             return u.size() > 0 ;
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login.", e);
        }
    }

    public Usuario pesquisarPorLoginESenha(String login, String senha) throws DAOException{
        try {
            return (Usuario) em.createQuery("select u from Usuario u where u.login =:login and u.senha =:senha")
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login e senha.", e);
        }
    }
    
    public boolean haUsuarioComEsteLogin(String login) throws DAOException{
        try {
            return em.createQuery("select u from Usuario u where u.login =:login")
                    .setParameter("login", login)
                    .getResultList().size() >= 1;
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login.", e);
        }
    }
    
    public int removerUusarioPorLogin(String login) throws DAOException{
        try {
            return em.createQuery("DELETE FROM Usuario where login =:login")
                    .setParameter("login", login)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login.", e);
        }
    }
}
