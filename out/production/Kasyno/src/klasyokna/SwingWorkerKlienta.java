package klasyokna;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static klasyokna.zwrotKabiny.lewy;

public class SwingWorkerKlienta extends SwingWorker<Object,Object> {

    Klient klient;
    Kolejki kolejki;
    public int numerWKolejce; //do kasjera
    private MaszynyDoGry mojaKabina;


    public int numerWKolejceDoSchodow; //ogólny wspólny dla obu stron
    public int numerWKolejceDoSchodowJednejStrony; // lokalny numerek do schodów od dołu lub od góry

    public SwingWorkerKlienta(Klient klient, Kolejki kolejki, int numer){
        this.klient = klient;
        this.kolejki = kolejki;
        numerWKolejce = numer;
        numerWKolejceDoSchodow = -1;
    }

    private void czekaj() throws InterruptedException {
        Thread.sleep(15) ;  // jest używany do poruszania się (tylko, ale mogę się mylić :P)
    }
    private void czekaj(long time) throws InterruptedException {
        Thread.sleep(time) ;
    }
    public void ruszDo(Integer celX, Integer celY) throws InterruptedException {
        if (celY != null) {
            while (klient.getY() != celY) {
                if (klient.getY() > celY)
                    klient.move(0, -1);
                else
                    klient.move(0, 1);
                czekaj();
            }
        }
        if (celX != null) {
            while (klient.getX() != celX) {
                if (klient.getX() > celX)
                    klient.move(-1, 0);
                else
                    klient.move(1, 0);
                czekaj();
            }
        }
    }

    public void zajmijMiejscePoWejsciu() throws InterruptedException {
        kolejki.zajmijMiejsce(numerWKolejce, kolejki.ktosPrzyKolejceDoKasy);
        Kolejki.zwiekszLiczbaWKolejce(1);
        while (kolejki.getLiczbaOsobDoKasy() < numerWKolejce)
        {
            zajmijMiejsce();
        }
    }
    public void zajmijMiejsce() throws InterruptedException {
            kolejki.sprawdzamKolejke.acquire();
                kolejki.zajmijMiejsce(numerWKolejce -1, kolejki.ktosPrzyKolejceDoKasy);
                kolejki.setKtosPrzyKolejce(numerWKolejce, false);
                numerWKolejce--;
            kolejki.sprawdzamKolejke.release();
            ruszDo(450 - numerWKolejce*45,null);
    }

    @Override
    protected Object doInBackground() throws Exception {
        czekaj(klient.getInitialSleep()); // zeby
        ruszDo(null, 605); //do wejścia
        ruszDo(450 - numerWKolejce * 45, 605);
        //while(klient.getCoins() < klient.getGoal()) { // próba zapętlenia daje nie przewidziany rezultat - trzeba poprawić kod żeby to wprowadzić

             //do kasy
            zajmijMiejscePoWejsciu();
            dzialajWKolejceDoKasy();

            //przejscie do schodow i wziecie "numerka" do nich

            znajdzMiejscePrzedSchodami(true);

            //czy moge przejść, ustawianie sie w kolejce i przechodzenie przez schody ("wąskie gardło")
            przejdzPrzezSchodyOdDolu();

            znajdzWolnaKabine();
            podejdzDoMojejKabiny();

            graj();


            znajdzMiejscePrzedSchodami(false);
            ruszDo(305, 400 - (numerWKolejceDoSchodowJednejStrony * 45));

            //czy moge przejść, ustawianie sie w kolejce i przechodzenie przez schody ("wąskie gardło")
            przejdzPrzezSchodyOdGory();
        //}

        //tutaj mozna dorobic co ma dalej zrobi po przejsciu przez schody!
        ruszDo(0, null);
        ruszDo(null, - 50);

        return null;
    }


    //przejdzPrzezSchodyOdGory i przejdzPrzezSchodyOdDolu są bardzo podobne,
    // wiec mozna je przerobic aby były te same tylko z warunkiem je zmieniajace czyOdDolu
    private void przejdzPrzezSchodyOdGory() throws InterruptedException {
        while (numerWKolejceDoSchodow >= 0 && numerWKolejceDoSchodowJednejStrony >= 0) {
            kolejki.zajmijMiejsce(numerWKolejceDoSchodowJednejStrony, kolejki.ktosPrzyKolejceOdGory);
            ruszDo(305, 400 - (numerWKolejceDoSchodowJednejStrony*45));

            if (numerWKolejceDoSchodow == 0 ) { //&& numerWKolejceDoSchodowJednejStrony == 0
                kolejki.schody.acquire();
                ruszDo(305, 505);
                kolejki.zwolnijKolejkeDoSchodow(this, false);
                kolejki.schody.release();
            }
            else if(!kolejki.czyKtosPrzyKolejceSchodowObustronnie(numerWKolejceDoSchodow - 1))
            {
                kolejki.sprawdzamKolejkeDoSchodow.acquire();
                kolejki.zajmijMiejsce(numerWKolejceDoSchodow -1, kolejki.ktosPrzyKolejceSchody);
                kolejki.setKtosPrzyKolejceSchodowObustronnie(numerWKolejceDoSchodow, false);
                numerWKolejceDoSchodow--;
                kolejki.sprawdzamKolejkeDoSchodow.release();
                czekaj();
            }
            else if(!kolejki.czyKtosPrzyKolejceSchodow(numerWKolejceDoSchodowJednejStrony - 1, false))
            {
                //System.out.println("Wątek" + numerWKolejceDoSchodow);
                kolejki.sprawdzamKolejkeDoSchodow.acquire();
                kolejki.zajmijMiejsce(numerWKolejceDoSchodowJednejStrony -1, kolejki.ktosPrzyKolejceOdGory);
                kolejki.setKtosPrzyKolejceSchodow(numerWKolejceDoSchodowJednejStrony, false, false);
                //numerWKolejceDoSchodow--;
                numerWKolejceDoSchodowJednejStrony--;
                kolejki.sprawdzamKolejkeDoSchodow.release();
                ruszDo(305, 400 - (numerWKolejceDoSchodowJednejStrony * 45));
                //System.out.println("Wątek, moja liczba to" + numerWKolejceDoSchodow + " " + numerWKolejceDoSchodowJednejStrony);
                czekaj(200);
            }
            czekaj(200);
        }
    }

    private void przejdzPrzezSchodyOdDolu() throws InterruptedException {
        while (numerWKolejceDoSchodow >= 0 && numerWKolejceDoSchodowJednejStrony >= 0) {
            kolejki.zajmijMiejsce(numerWKolejceDoSchodowJednejStrony, kolejki.ktosPrzyKolejceOdDolu);
            ruszDo(350 + (numerWKolejceDoSchodowJednejStrony * 45), null);

            if (numerWKolejceDoSchodow == 0) { // && numerWKolejceDoSchodowJednejStrony == 0
                kolejki.schody.acquire();
                ruszDo(350, null);
                ruszDo(null, 400);

                kolejki.zwolnijKolejkeDoSchodow(this, true);
                kolejki.schody.release();
                //System.out.println("Wątek" + numerWKolejceDoSchodow);
            }
            else if(!kolejki.czyKtosPrzyKolejceSchodowObustronnie(numerWKolejceDoSchodow - 1))
            {
                kolejki.sprawdzamKolejkeDoSchodow.acquire();
                kolejki.zajmijMiejsce(numerWKolejceDoSchodow -1, kolejki.ktosPrzyKolejceSchody);
                kolejki.setKtosPrzyKolejceSchodowObustronnie(numerWKolejceDoSchodow, false);
                numerWKolejceDoSchodow--;
                kolejki.sprawdzamKolejkeDoSchodow.release();
                czekaj();
            }
            else if(!kolejki.czyKtosPrzyKolejceSchodow(numerWKolejceDoSchodowJednejStrony - 1, true))
            {
                //System.out.println("Wątek" + numerWKolejceDoSchodow);
                kolejki.sprawdzamKolejkeDoSchodow.acquire();
                    kolejki.zajmijMiejsce(numerWKolejceDoSchodowJednejStrony -1, kolejki.ktosPrzyKolejceOdDolu);
                    kolejki.setKtosPrzyKolejceSchodow(numerWKolejceDoSchodowJednejStrony, false, true);
                    //numerWKolejceDoSchodow--;
                    numerWKolejceDoSchodowJednejStrony--;
                kolejki.sprawdzamKolejkeDoSchodow.release();
                ruszDo(350 - numerWKolejceDoSchodowJednejStrony*45,null);
                //System.out.println("Wątek, moja liczba to" + numerWKolejceDoSchodow + " " + numerWKolejceDoSchodowJednejStrony);
                czekaj(200);
            }
            czekaj(200);
        }
    }

    private void dzialajWKolejceDoKasy() throws InterruptedException {
        while (numerWKolejce >= 0)
        {

            kolejki.zajmijMiejsce(numerWKolejce, kolejki.ktosPrzyKolejceDoKasy);
            ruszDo(450 - numerWKolejce*45,null);
            //czekaj(800);
            if(numerWKolejce == 0 && klient.getX() == 450)
            {

                kolejki.kup.acquire();
                klient.setColor(Color.YELLOW);
                kolejki.zakupMonety(klient);
                kolejki.przyniesDoKasy.release();
                ruszDo(null, 505);
                kolejki.zwolnijKolejke(this);
            }
            else if(!kolejki.czyKtosPrzyKolejce(numerWKolejce - 1) && numerWKolejce !=0)
            {
                zajmijMiejsce();
                czekaj(200);
            }
            czekaj(200);
        }
    }

    private void znajdzMiejscePrzedSchodami(boolean czyOdDolu) throws InterruptedException {
        Para<Integer> schodyNumery = kolejki.zajmijKolejkeDoSchodow(czyOdDolu);
        numerWKolejceDoSchodow = schodyNumery.getX();
        numerWKolejceDoSchodowJednejStrony = schodyNumery.getY();
    }

    private void graj() throws InterruptedException {
        Random gen = new Random();
        while (klient.getCoins() != klient.getLoss() && klient.getCoins() < klient.getGoal())
        {
            int i = gen.nextInt(15);
            if (i == 0) //to i tak uczciwie w porównaniu do normalnych kasyn :P
                klient.changeCoins(5);
            else if (i>0 && i<3) //no dobra moga troszke wygrywac, aby dluzej grali
            {
                klient.changeCoins(1);
            }
            else
                klient.changeCoins(-1);
            czekaj(400);
        }
        if (klient.getCoins() >= klient.getGoal())
            klient.setColor(Color.GREEN);
        else {
            klient.setColor(Color.RED);
            klient.setGoal(klient.getGoal() + 5);  //jesli dorobi sie petle, aby wracali poprawnie do kasy to będą chcieli odegrać przegraną ;)
        }
        mojaKabina.zwalniam();
        if (mojaKabina.getZwrotKabiny() == lewy) //aby odeszli trochę od kabiny aby nie wchodzili na innych graczy
        {
            ruszDo(klient.getX() - 50, null);
        }
        else
            ruszDo(klient.getX() + 50, null);
    }

    private void podejdzDoMojejKabiny() throws InterruptedException {
        Para<Integer> pozycjaGrania = mojaKabina.getPozycjaGrania();
        int x = pozycjaGrania.getX();
        int y = pozycjaGrania.getY();
        ruszDo(x, y);
    }

    private void znajdzWolnaKabine() {
        Random gen = new Random();
        int i = gen.nextInt(12);
        while (i < 12)
        {
            MaszynyDoGry m = kolejki.getListaMaszyn().get(i);
            if (m.czyAktywny && m.czyWolny)
            {
                mojaKabina = m;
                break;
            }
            i++;
            if(i>=12) i = 0;
        }
        mojaKabina.czyWolny = false;
    }
}
