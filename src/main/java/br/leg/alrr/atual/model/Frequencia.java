package br.leg.alrr.atual.model;

import br.leg.alrr.common.util.BaseEntity;
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Heliton Nascimento
 */
@Entity
@Table(name = "frequencia")
public class Frequencia implements Serializable, BaseEntity, Comparable<Frequencia> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean presenca1;
    private boolean presenca2;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Ouvinte ouvinte;
    
    //========================================================================//
    public Frequencia() {
    }

    public Frequencia(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPresenca1() {
        return presenca1;
    }

    public void setPresenca1(boolean presenca1) {
        this.presenca1 = presenca1;
    }

    public boolean isPresenca2() {
        return presenca2;
    }

    public void setPresenca2(boolean presenca2) {
        this.presenca2 = presenca2;
    }

    public Ouvinte getOuvinte() {
        return ouvinte;
    }

    public void setOuvinte(Ouvinte ouvinte) {
        this.ouvinte = ouvinte;
    }

    @Override
    public int compareTo(Frequencia o) {
        if (this.id.equals(o.getId())) {
            return 0;
        } else if (this.id > o.getId()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Frequencia other = (Frequencia) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
