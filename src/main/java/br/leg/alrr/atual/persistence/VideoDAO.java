package br.leg.alrr.atual.persistence;

import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.atual.model.Video;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class VideoDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Video o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar video.", e);
        }
    }

    public void atualizar(Video o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar video.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Video o order by o.id DESC").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public Video listarOUltimoRegistro() throws DAOException{
        try {
            return (Video) em.createQuery("select o from Video o order by o.id desc").getResultList().get(0);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public void remover(Video o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover video.", e);
        }
    }
}
