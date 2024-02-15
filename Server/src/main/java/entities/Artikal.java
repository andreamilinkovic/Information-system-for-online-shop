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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Andrea
 */
@Entity
@Table(name = "artikal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Artikal.findAll", query = "SELECT a FROM Artikal a"),
    @NamedQuery(name = "Artikal.findBySifA", query = "SELECT a FROM Artikal a WHERE a.sifA = :sifA"),
    @NamedQuery(name = "Artikal.findByNaziv", query = "SELECT a FROM Artikal a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "Artikal.findByOpis", query = "SELECT a FROM Artikal a WHERE a.opis = :opis"),
    @NamedQuery(name = "Artikal.findByCena", query = "SELECT a FROM Artikal a WHERE a.cena = :cena"),
    @NamedQuery(name = "Artikal.findByPopust", query = "SELECT a FROM Artikal a WHERE a.popust = :popust")})
public class Artikal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sifA")
    private Integer sifA;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "opis")
    private String opis;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cena")
    private BigDecimal cena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "popust")
    private int popust;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sifA")
    private List<Recenzija> recenzijaList;
    @JoinColumn(name = "sifK", referencedColumnName = "sifK")
    @ManyToOne(optional = false)
    private Kategorija sifK;
    @JoinColumn(name = "sifP", referencedColumnName = "sifK")
    @ManyToOne(optional = false)
    private Korisnik sifP;
    @OneToMany(mappedBy = "sifA")
    private List<KorpaArtikal> korpaArtikalList;

    public Artikal() {
    }

    public Artikal(Integer sifA) {
        this.sifA = sifA;
    }

    public Artikal(Integer sifA, String naziv, String opis, BigDecimal cena, int popust) {
        this.sifA = sifA;
        this.naziv = naziv;
        this.opis = opis;
        this.cena = cena;
        this.popust = popust;
    }

    public Integer getSifA() {
        return sifA;
    }

    public void setSifA(Integer sifA) {
        this.sifA = sifA;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public int getPopust() {
        return popust;
    }

    public void setPopust(int popust) {
        this.popust = popust;
    }

    @XmlTransient
    public List<Recenzija> getRecenzijaList() {
        return recenzijaList;
    }

    public void setRecenzijaList(List<Recenzija> recenzijaList) {
        this.recenzijaList = recenzijaList;
    }

    public Kategorija getSifK() {
        return sifK;
    }

    public void setSifK(Kategorija sifK) {
        this.sifK = sifK;
    }

    public Korisnik getSifP() {
        return sifP;
    }

    public void setSifP(Korisnik sifP) {
        this.sifP = sifP;
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
        hash += (sifA != null ? sifA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artikal)) {
            return false;
        }
        Artikal other = (Artikal) object;
        if ((this.sifA == null && other.sifA != null) || (this.sifA != null && !this.sifA.equals(other.sifA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Artikal[ sifA=" + sifA + " ]";
    }
    
}
