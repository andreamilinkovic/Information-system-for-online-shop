/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "grad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grad.findAll", query = "SELECT g FROM Grad g"),
    @NamedQuery(name = "Grad.findBySifG", query = "SELECT g FROM Grad g WHERE g.sifG = :sifG"),
    @NamedQuery(name = "Grad.findByNaziv", query = "SELECT g FROM Grad g WHERE g.naziv = :naziv")})
public class Grad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sifG")
    private Integer sifG;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "naziv")
    private String naziv;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sifG")
    private List<Narudzbina> narudzbinaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sifG")
    private List<Korisnik> korisnikList;

    public Grad() {
    }

    public Grad(Integer sifG) {
        this.sifG = sifG;
    }

    public Grad(Integer sifG, String naziv) {
        this.sifG = sifG;
        this.naziv = naziv;
    }

    public Integer getSifG() {
        return sifG;
    }

    public void setSifG(Integer sifG) {
        this.sifG = sifG;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @XmlTransient
    public List<Narudzbina> getNarudzbinaList() {
        return narudzbinaList;
    }

    public void setNarudzbinaList(List<Narudzbina> narudzbinaList) {
        this.narudzbinaList = narudzbinaList;
    }

    @XmlTransient
    public List<Korisnik> getKorisnikList() {
        return korisnikList;
    }

    public void setKorisnikList(List<Korisnik> korisnikList) {
        this.korisnikList = korisnikList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifG != null ? sifG.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grad)) {
            return false;
        }
        Grad other = (Grad) object;
        if ((this.sifG == null && other.sifG != null) || (this.sifG != null && !this.sifG.equals(other.sifG))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Grad[ sifG=" + sifG + " ]";
    }
    
}
