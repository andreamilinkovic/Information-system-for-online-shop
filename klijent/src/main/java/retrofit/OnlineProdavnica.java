/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *
 * @author Andrea
 */
public interface OnlineProdavnica {
    
    //podsistem1
    @POST("gradovi/{naziv}")
    Call<String> kreirajGrad(@Path("naziv") String naziv);
    
    @POST("korisnici/{korisnicko_ime}/{sifra}/{ime}/{prezime}/{adresa}/{novac}/{grad}")
    Call<String> kreirajKorisnika(@Path("korisnicko_ime") String korisnicko_ime, @Path("sifra") String sifra,
            @Path("ime") String ime, @Path("prezime") String prezime, @Path("adresa") String adresa, 
            @Path("novac") double novac, @Path("grad") int grad);
    
    @POST("korisnici/{korisnicko_ime}/{novac}")
    Call<String> dodajNovac(@Path("korisnicko_ime") String korisnicko_ime, @Path("novac") double novac);
     
    @POST("korisnici/{korisnicko_ime}/{adresa}/{grad}")
    Call<String> promeniAdresuIGrad(@Path("korisnicko_ime") String korisnicko_ime, @Path("adresa") String adresa, @Path("grad") int grad);
    
    @GET("gradovi")
    Call<String> dohvatiGradove();
    
    @GET("korisnici")
    Call<String> dohvatiKorisnike();
    
    //podsistem2
    @POST("kategorije/{kategorija}/{natkategorija}")
    Call<String> kreirajKategoriju(@Path("kategorija") String kategorija, @Path("natkategorija") int natkategorija);
    
    @POST("artikli/{naziv}/{opis}/{cena}/{kategorija}/{korisnicko_ime}")
    Call<String> kreirajArtikal(@Path("naziv") String naziv, @Path("opis") String opis,
            @Path("cena") double cena, @Path("kategorija") int kategorija, @Path("korisnicko_ime") String korisnicko_ime);
    
    @POST("artikli/cena/{sifA}/{cena}")
    Call<String> promeniCenuArtikla(@Path("sifA") int sifA, @Path("cena") double cena);
    
    @POST("artikli/popust/{sifA}/{popust}")
    Call<String> postaviPopustNaArtikal(@Path("sifA") int sifA, @Path("popust") int popust);
    
    @POST("artikli/dodaj/{sifA}/{kolicina}/{korisnicko_ime}")
    Call<String> dodajArtikalUKorpu(@Path("sifA") int sifA, @Path("kolicina") int kolicina, @Path("korisnicko_ime") String korisnicko_ime);
    
    @POST("artikli/izbaci/{sifA}/{kolicina}/{korisnicko_ime}")
    Call<String> izbaciArtikalIzKorpe(@Path("sifA") int sifA, @Path("kolicina") int kolicina, @Path("korisnicko_ime") String korisnicko_ime);
    
    @GET("kategorije")
    Call<String> dohvatiKategorije();
    
    @GET("artikli/mojiartikli")
    Call<String> dohvatiMojeArtikle();
    
    @GET("artikli/mojakorpa")
    Call<String> dohvatiMojuKorpu();
    
    //podsistem3
    @POST("placanje/{korisnicko_ime}")
    Call<String> plati(@Path("korisnicko_ime") String korisnicko_ime);
    
    @GET("placanje/mojenarudzbine")
    Call<String> dohvatiMojeNarudzbine();
    
    @GET("placanje/narudzbine")
    Call<String> dohvatiNarudzbine();
    
    @GET("placanje/transakcije")
    Call<String> dohvatiTransakcije();
}
