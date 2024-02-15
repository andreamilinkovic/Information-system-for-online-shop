/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klijent;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import retrofit.OnlineProdavnica;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * @author Andrea
 */
public class Klijent {
    
    public static void main(String[] args){
        
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS).build();
        
        //retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/Server/resources/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        
        OnlineProdavnica server = retrofit.create(OnlineProdavnica.class);
        
        //variables
        boolean end = false;
        Scanner scanner = new Scanner(System.in);
        Call<String> response;
        String korisnicko_ime, sifra, ime, prezime, adresa;
        double suma;
        
        String naziv_kategorije, opis;
        double cena;
        int sif_natkategorije, id, popust, kolicina;
        
        //main part
        while(!end){
            try {
                System.out.println("\nIzaberite jedan od zahteva:\n\n" +
                        "1. Kreiranje grada\n" +
                        "2. Kreiranje korisnika\n" +
                        "3. Dodavanje novca korisniku\n" +
                        "4. Promena adrese i grada za korisnika\n" +
                        "5. Kreiranje kategorije\n" +
                        "6. Kreiranje artikla\n" +
                        "7. Menjanje cene artikla\n" +
                        "8. Postavljanje popusta za artikal\n" +
                        "9. Dodavanje artikala u odredjenoj kolicini u korpu\n" +
                        "10. Brisanje artikala u odredjenoj kolicini iz korpe\n" +
                        "11. Placanje\n" +
                        "12. Dohvatanje svih gradova\n" +
                        "13. Dohvatanje svih korisnika\n" +
                        "14. Dohvatanje svih kategorija\n" +
                        "15. Dohvatanje svih artikala koje prodaje korisnik koji je poslao zahtev\n" +
                        "16. Dohvatanje sadrzaja korpe korisnika koji je poslao zahtev\n" +
                        "17. Dohvatanje svih narudzbina korisnika koji je poslao zahtev\n" +
                        "18. Dohvatanje svih narudzbina\n" +
                        "19. Dohvatanje svih transakcija\n" +
                        "20. Kraj\n");
                
                int request = Integer.parseInt(scanner.nextLine());
                
                switch(request){
                    case 1:
                        System.out.println("Unesite naziv grada:");
                        String naziv = scanner.nextLine();
                        
                        response = server.kreirajGrad(naziv);
                        System.out.println(response.execute().body());
                        break;
                    case 2:
                        System.out.println("Unesite korisnicko ime:");
                        korisnicko_ime = scanner.nextLine();
                        System.out.println("Unesite lozinku:");
                        sifra = scanner.nextLine();
                        System.out.println("Unesite ime:");
                        ime = scanner.nextLine();
                        System.out.println("Unesite prezime:");
                        prezime = scanner.nextLine();
                        System.out.println("Unesite adresu:");
                        adresa = scanner.nextLine();
                        System.out.println("Unesite id grada:");
                        id = Integer.parseInt(scanner.nextLine());
                        
                        response = server.kreirajKorisnika(korisnicko_ime, sifra, ime, prezime, adresa, 0, id);
                        System.out.println(response.execute().body());
                        break;
                    case 3:
                        System.out.println("Unesite korisnicko ime:");
                        korisnicko_ime = scanner.nextLine();
                        System.out.println("Unesite sumu:");
                        suma = Double.parseDouble(scanner.nextLine());
                        
                        response = server.dodajNovac(korisnicko_ime, suma);
                        System.out.println(response.execute().body());
                        break;
                    case 4:
                        System.out.println("Unesite korisnicko ime:");
                        korisnicko_ime = scanner.nextLine();
                        System.out.println("Unesite adresu:");
                        adresa = scanner.nextLine();
                        System.out.println("Unesite id grada:");
                        id = Integer.parseInt(scanner.nextLine());
                        
                        response = server.promeniAdresuIGrad(korisnicko_ime, adresa, id);
                        System.out.println(response.execute().body());
                        break;
                    case 5:
                        System.out.println("Unesite naziv kategorije:");
                        naziv_kategorije = scanner.nextLine();
                        System.out.println("Unesite sifru natkategorije ako postoji, a ako ne postoji -1:");
                        sif_natkategorije = Integer.parseInt(scanner.nextLine());
                        
                        response = server.kreirajKategoriju(naziv_kategorije, sif_natkategorije);
                        System.out.println(response.execute().body());
                        break;
                    case 6:
                        System.out.println("Unesite naziv artikla:");
                        naziv_kategorije = scanner.nextLine();
                        System.out.println("Unesite opis artikla:");
                        opis = scanner.nextLine();
                        System.out.println("Unesite cenu artikla:");
                        cena = Double.parseDouble(scanner.nextLine());
                        System.out.println("Unesite sifru kategorije:");
                        sif_natkategorije = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite korisnicko ime prodavca artikla:");
                        korisnicko_ime = scanner.nextLine();
                        
                        response = server.kreirajArtikal(naziv_kategorije, opis, cena, sif_natkategorije, korisnicko_ime);
                        System.out.println(response.execute().body());
                        break;
                    case 7:
                        System.out.println("Unesite id artikla:");
                        id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite novu cenu artikla:");
                        cena = Double.parseDouble(scanner.nextLine());
                        
                        response = server.promeniCenuArtikla(id, cena);
                        System.out.println(response.execute().body());
                        break;
                    case 8:
                        System.out.println("Unesite id artikla:");
                        id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite vrednost popusta na artikal:");
                        popust = Integer.parseInt(scanner.nextLine());
                        
                        response = server.postaviPopustNaArtikal(id, popust);
                        System.out.println(response.execute().body());
                        break;
                    case 9:
                        System.out.println("Unesite id artikla:");
                        id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite kolicinu koju zelite da dodate u korpu:");
                        kolicina = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite korisnicko ime:");
                        korisnicko_ime = scanner.nextLine();
                        
                        response = server.dodajArtikalUKorpu(id, kolicina, korisnicko_ime);
                        System.out.println(response.execute().body());
                        break;
                    case 10:
                        System.out.println("Unesite id artikla:");
                        id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite kolicinu koju zelite da izbacite iz korpe:");
                        kolicina = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite korisnicko ime:");
                        korisnicko_ime = scanner.nextLine();
                        
                        response = server.izbaciArtikalIzKorpe(id, kolicina, korisnicko_ime);
                        System.out.println(response.execute().body());
                        break;
                    case 11:
                        System.out.println("Unesite korisnicko ime:");
                        korisnicko_ime = scanner.nextLine();
                        
                        response = server.plati(korisnicko_ime);
                        System.out.println(response.execute().body());
                        break;
                    case 12:
                        response = server.dohvatiGradove();
                        System.out.println(response.execute().body());
                        break;
                    case 13:
                        response = server.dohvatiKorisnike();
                        System.out.println(response.execute().body());
                        break;
                    case 14:
                        response = server.dohvatiKategorije();
                        System.out.println(response.execute().body());
                        break;
                    case 15:
                        response = server.dohvatiMojeArtikle();
                        System.out.println(response.execute().body());
                        break;
                    case 16:
                        response = server.dohvatiMojuKorpu();
                        System.out.println(response.execute().body());
                        break;
                    case 17:
                        response = server.dohvatiMojeNarudzbine();
                        System.out.println(response.execute().body());
                        break;
                    case 18:
                        response = server.dohvatiNarudzbine();
                        System.out.println(response.execute().body());
                        break;
                    case 19:
                        response = server.dohvatiTransakcije();
                        System.out.println(response.execute().body());
                        break;
                    case 20:
                        end = true;
                        break;
                    default:
                        System.out.println("Zahtev ne postoji.");
                        break;
                }
                
                Thread.sleep(2000);
                
            } catch (IOException ex) {
                Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
