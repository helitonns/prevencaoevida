package br.leg.alrr.atual.persistence;

import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.atual.model.Evento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class EventoDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Evento o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar evento.", e);
        }
    }

    public void atualizar(Evento o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar evento.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Evento o order by o.id DESC").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public Evento listarOUltimoRegistro() throws DAOException{
        try {
            return (Evento) em.createQuery("select o from Evento o order by o.id desc").getResultList().get(0);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public void remover(Evento o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover evento.", e);
        }
    }
}
