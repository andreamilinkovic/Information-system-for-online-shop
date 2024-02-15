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
public class Request1 implements Serializable{
    private int request_no;
    
    private String korisnicko_ime;
    private String sifra;
    private String ime;
    private String prezime;
    private String adresa;
    private double novac;
    private String naziv_grada;
    private int sif_grada;

    public int getRequest_no() {
        return request_no;
    }

    public void setRequest_no(int request_no) {
        this.request_no = request_no;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public double getNovac() {
        return novac;
    }

    public void setNovac(double novac) {
        this.novac = novac;
    }

    public int getSif_grada() {
        return sif_grada;
    }

    public void setSif_grada(int sif_grada) {
        this.sif_grada = sif_grada;
    }

    public String getNaziv_grada() {
        return naziv_grada;
    }

    public void setNaziv_grada(String naziv_grada) {
        this.naziv_grada = naziv_grada;
    }

}
