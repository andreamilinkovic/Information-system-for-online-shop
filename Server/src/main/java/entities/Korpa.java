/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Andrea
 */
@Entity
@Table(name = "korpa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Korpa.findAll", query = "SELECT k FROM Korpa k"),
    @NamedQuery(name = "Korpa.findBySifK", query = "SELECT k FROM Korpa k WHERE k.sifK = :sifK"),
    @NamedQuery(name = "Korpa.findByCena", query = "SELECT k FROM Korpa k WHERE k.cena = :cena")})
public class Korpa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sifK")
    private Integer sifK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cena")
    private BigDecimal cena;
    @JoinColumn(name = "sifK", referencedColumnName = "sifK", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Korisnik korisnik;
    @OneToMany(mappedBy = "sifK")
    private List<KorpaArtikal> korpaArtikalList;

    public Korpa() {
    }

    public Korpa(Integer sifK) {
        this.sifK = sifK;
    }

    public Korpa(Integer sifK, BigDecimal cena) {
        this.sifK = sifK;
        this.cena = cena;
    }

    public Integer getSifK() {
        return sifK;
    }

    public void setSifK(Integer sifK) {
        this.sifK = sifK;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    @XmlTransient
    public List<KorpaArtikal> getKorpaArtikalList() {
        return korpaArtikalList;
    }

    public void setKorpaArtikalList(List<KorpaArtikal> korpaArtikalList) {
        this.korpaArtikalList = korpaArtikalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifK != null ? sifK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Korpa)) {
            return false;
        }
        Korpa other = (Korpa) object;
        if ((this.sifK == null && other.sifK != null) || (this.sifK != null && !this.sifK.equals(other.sifK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Korpa[ sifK=" + sifK + " ]";
    }
    
}
