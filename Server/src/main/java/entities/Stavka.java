/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andrea
 */
@Entity
@Table(name = "stavka")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stavka.findAll", query = "SELECT s FROM Stavka s"),
    @NamedQuery(name = "Stavka.findBySifS", query = "SELECT s FROM Stavka s WHERE s.sifS = :sifS"),
    @NamedQuery(name = "Stavka.findByCena", query = "SELECT s FROM Stavka s WHERE s.cena = :cena"),
    @NamedQuery(name = "Stavka.findByKolicina", query = "SELECT s FROM Stavka s WHERE s.kolicina = :kolicina"),
    @NamedQuery(name = "Stavka.findBySifA", query = "SELECT s FROM Stavka s WHERE s.sifA = :sifA")})
public class Stavka implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sifS")
    private Integer sifS;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cena")
    private BigDecimal cena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kolicina")
    private int kolicina;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sifA")
    private int sifA;
    @JoinColumn(name = "sifN", referencedColumnName = "sifN")
    @ManyToOne(optional = false)
    private Narudzbina sifN;

    public Stavka() {
    }

    public Stavka(Integer sifS) {
        this.sifS = sifS;
    }

    public Stavka(Integer sifS, BigDecimal cena, int kolicina, int sifA) {
        this.sifS = sifS;
        this.cena = cena;
        this.kolicina = kolicina;
        this.sifA = sifA;
    }

    public Integer getSifS() {
        return sifS;
    }

    public void setSifS(Integer sifS) {
        this.sifS = sifS;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public int getSifA() {
        return sifA;
    }

    public void setSifA(int sifA) {
        this.sifA = sifA;
    }

    public Narudzbina getSifN() {
        return sifN;
    }

    public void setSifN(Narudzbina sifN) {
        this.sifN = sifN;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifS != null ? sifS.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stavka)) {
            return false;
        }
        Stavka other = (Stavka) object;
        if ((this.sifS == null && other.sifS != null) || (this.sifS != null && !this.sifS.equals(other.sifS))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Stavka[ sifS=" + sifS + " ]";
    }
    
}
