package br.leg.alrr.atual.persistence;

import br.leg.alrr.atual.model.Ouvinte;
import br.leg.alrr.common.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class OuvinteDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Ouvinte o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar ouvinte.", e);
        }
    }

    public void atualizar(Ouvinte o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar ouvinte.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Ouvinte o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public Ouvinte buscarPorEmail(String email) throws DAOException{
        try {
            return (Ouvinte) em.createQuery("select o from Ouvinte o where o.email = :email order by o.nome asc")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar ouvinte.", e);
        }
    }
    
    public boolean haOvinteComCPF(String cpf) throws DAOException{
        try {
            return em.createQuery("select o from Ouvinte o where o.cpf = :cpf")
                    .setParameter("cpf", cpf)
                    .getResultList().size() > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar ouvinte.", e);
        }
    }
    
    public void remover(Ouvinte o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover ouvinte.", e);
        }
    }
    
    public Long quantidadePresencial() throws DAOException{
        try {
            return (Long) em.createQuery("select COUNT(o) from Ouvinte o where o.presencial = true").getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
}
