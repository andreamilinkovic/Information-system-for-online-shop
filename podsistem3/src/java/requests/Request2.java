/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.io.Serializable;

/**
 *
 * @author Andrea
 */
public class Request2 implements Serializable{
    private int request_no;
    
    private String naziv;
    private int natkategorija_sif;

    private String opis;
    private double cena;
    private int popust;
    private String korisnicko_ime;
    
    private int ocena;
    private int sif_artikal;
    
    private int kolicina;
    private int sif_korpa;
    
    private double jedinicna_cena;

    public int getRequest_no() {
        return request_no;
    }

    public void setRequest_no(int request_no) {
        this.request_no = request_no;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getNatkategorija_sif() {
        return natkategorija_sif;
    }

    public void setNatkategorija_sif(int natkategorija_sif) {
        this.natkategorija_sif = natkategorija_sif;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public int getPopust() {
        return popust;
    }

    public void setPopust(int popust) {
        this.popust = popust;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public int getSif_artikal() {
        return sif_artikal;
    }

    public void setSif_artikal(int sif_artikal) {
        this.sif_artikal = sif_artikal;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public int getSif_korpa() {
        return sif_korpa;
    }

    public void setSif_korpa(int sif_korpa) {
        this.sif_korpa = sif_korpa;
    }

    public double getJedinicna_cena() {
        return jedinicna_cena;
    }

    public void setJedinicna_cena(double jedinicna_cena) {
        this.jedinicna_cena = jedinicna_cena;
    }
    
}
