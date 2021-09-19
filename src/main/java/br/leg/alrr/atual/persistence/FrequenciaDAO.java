package br.leg.alrr.atual.persistence;

import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.atual.model.Frequencia;
import br.leg.alrr.atual.model.Ouvinte;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class FrequenciaDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Frequencia o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar frequência.", e);
        }
    }

    public void atualizar(Frequencia o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar frequência.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Frequencia o order by o.ouvinte.nome").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public List listarTiposPresenca(boolean tipoPresenca) throws DAOException {
        try {
            return em.createQuery("select o from Frequencia o where o.ouvinte.presencial = :tipo")
                    .setParameter("tipo",tipoPresenca)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar os tipos de presença.", e);
        }
    }
    
    public Frequencia buscarPorOuvinte(Ouvinte ouvinte) throws DAOException{
        try {
            return (Frequencia) em.createQuery("select o from Frequencia o where o.ouvinte.id = :idOuvinte")
                    .setParameter("idOuvinte", ouvinte.getId())
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public Frequencia listarOUltimoRegistro() throws DAOException{
        try {
            return (Frequencia) em.createQuery("select o from Frequencia o order by o.id desc").getResultList().get(0);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public boolean verificarSeOuvinteConfirmouPresenca(Ouvinte o) throws DAOException{
        try {
            Frequencia f = (Frequencia) em.createQuery("select o from Frequencia o where o.ouvinte.id = :idOuvinte and o.presenca1 = true")
                    .setParameter("idOuvinte", o.getId())
                    .getSingleResult();
            return f != null;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public void remover(Frequencia o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover frequência.", e);
        }
    }
}
