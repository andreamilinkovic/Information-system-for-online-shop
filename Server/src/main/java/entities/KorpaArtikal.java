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
@Table(name = "korpa_artikal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KorpaArtikal.findAll", query = "SELECT k FROM KorpaArtikal k"),
    @NamedQuery(name = "KorpaArtikal.findBySifKA", query = "SELECT k FROM KorpaArtikal k WHERE k.sifKA = :sifKA"),
    @NamedQuery(name = "KorpaArtikal.findByKolicina", query = "SELECT k FROM KorpaArtikal k WHERE k.kolicina = :kolicina"),
    @NamedQuery(name = "KorpaArtikal.findByCena", query = "SELECT k FROM KorpaArtikal k WHERE k.cena = :cena")})
public class KorpaArtikal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sifKA")
    private Integer sifKA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kolicina")
    private int kolicina;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cena")
    private BigDecimal cena;
    @JoinColumn(name = "sifA", referencedColumnName = "sifA")
    @ManyToOne
    private Artikal sifA;
    @JoinColumn(name = "sifK", referencedColumnName = "sifK")
    @ManyToOne
    private Korpa sifK;

    public KorpaArtikal() {
    }

    public KorpaArtikal(Integer sifKA) {
        this.sifKA = sifKA;
    }

    public KorpaArtikal(Integer sifKA, int kolicina, BigDecimal cena) {
        this.sifKA = sifKA;
        this.kolicina = kolicina;
        this.cena = cena;
    }

    public Integer getSifKA() {
        return sifKA;
    }

    public void setSifKA(Integer sifKA) {
        this.sifKA = sifKA;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public Artikal getSifA() {
        return sifA;
    }

    public void setSifA(Artikal sifA) {
        this.sifA = sifA;
    }

    public Korpa getSifK() {
        return sifK;
    }

    public void setSifK(Korpa sifK) {
        this.sifK = sifK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifKA != null ? sifKA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KorpaArtikal)) {
            return false;
        }
        KorpaArtikal other = (KorpaArtikal) object;
        if ((this.sifKA == null && other.sifKA != null) || (this.sifKA != null && !this.sifKA.equals(other.sifKA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.KorpaArtikal[ sifKA=" + sifKA + " ]";
    }
    
}
