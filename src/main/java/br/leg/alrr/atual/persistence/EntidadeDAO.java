package br.leg.alrr.atual.persistence;

import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.atual.model.Entidade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class EntidadeDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Entidade o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar entidade.", e);
        }
    }

    public void atualizar(Entidade o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar entidade.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Entidade o order by o.nome").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public Entidade listarOUltimoRegistro() throws DAOException{
        try {
            return (Entidade) em.createQuery("select o from Entidade o order by o.id desc").getResultList().get(0);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public void remover(Entidade o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover entidade.", e);
        }
    }
}
