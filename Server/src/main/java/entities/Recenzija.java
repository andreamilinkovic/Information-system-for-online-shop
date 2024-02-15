/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andrea
 */
@Entity
@Table(name = "recenzija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recenzija.findAll", query = "SELECT r FROM Recenzija r"),
    @NamedQuery(name = "Recenzija.findBySifR", query = "SELECT r FROM Recenzija r WHERE r.sifR = :sifR"),
    @NamedQuery(name = "Recenzija.findByOcena", query = "SELECT r FROM Recenzija r WHERE r.ocena = :ocena"),
    @NamedQuery(name = "Recenzija.findByOpis", query = "SELECT r FROM Recenzija r WHERE r.opis = :opis")})
public class Recenzija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sifR")
    private Integer sifR;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ocena")
    private int ocena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "opis")
    private String opis;
    @JoinColumn(name = "sifA", referencedColumnName = "sifA")
    @ManyToOne(optional = false)
    private Artikal sifA;

    public Recenzija() {
    }

    public Recenzija(Integer sifR) {
        this.sifR = sifR;
    }

    public Recenzija(Integer sifR, int ocena, String opis) {
        this.sifR = sifR;
        this.ocena = ocena;
        this.opis = opis;
    }

    public Integer getSifR() {
        return sifR;
    }

    public void setSifR(Integer sifR) {
        this.sifR = sifR;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Artikal getSifA() {
        return sifA;
    }

    public void setSifA(Artikal sifA) {
        this.sifA = sifA;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifR != null ? sifR.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recenzija)) {
            return false;
        }
        Recenzija other = (Recenzija) object;
        if ((this.sifR == null && other.sifR != null) || (this.sifR != null && !this.sifR.equals(other.sifR))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Recenzija[ sifR=" + sifR + " ]";
    }
    
}
