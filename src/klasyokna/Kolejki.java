package klasyokna;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Kolejki {
    static int liczbaOsobDoKasy = 0;
    static int liczbaWKolejceDoSchodow = 0;
    static int liczbaWKolejceDoSchodowOdDolu = 0;
    static int liczbaWKolejceDoSchodowOdGory = 0;
    public boolean[] ktosPrzyKolejceDoKasy;

    public boolean[] ktosPrzyKolejceSchody;
    public boolean[] ktosPrzyKolejceOdDolu;
    public boolean[] ktosPrzyKolejceOdGory;
    //public static boolean ktosSchodzi = false;

    final Semaphore kup = new Semaphore(0);
    final Semaphore przyniesDoKasy = new Semaphore(1);
    final Semaphore sprawdzamKolejke = new Semaphore(2);
    final Semaphore schody = new Semaphore(1);
    final Semaphore sprawdzamKolejkeDoSchodow = new Semaphore(1);
    final Semaphore pobierzNumer = new Semaphore(1);

    private Pracownik kasjer;
    private ArrayList<MaszynyDoGry> listaMaszyn;

    public ArrayList<MaszynyDoGry> getListaMaszyn() {
        return listaMaszyn;
    }

    public Kolejki(Pracownik kasjer, ArrayList<MaszynyDoGry> listaKabin)
    {

        this.kasjer = kasjer;
        this.listaMaszyn = listaKabin;

        ktosPrzyKolejceDoKasy = new boolean[11];

        ktosPrzyKolejceSchody = new boolean[10];
        ktosPrzyKolejceOdDolu = new boolean[5];
        ktosPrzyKolejceOdGory = new boolean[5];

    }

    public void zakupMonety(Klient klient)
    {
        kasjer.transakcja(-5);
        klient.changeCoins(5);
    }

    public void zajmijMiejsce(int numer, boolean[] kolejka)
    {
        if(numer >= 0)
            kolejka[numer] = true;
    }


    public void setKtosPrzyKolejce(int i, boolean naCo) {
        ktosPrzyKolejceDoKasy[i] = naCo;
    }

    public boolean czyKtosPrzyKolejce(int i) {
        return ktosPrzyKolejceDoKasy[i];
    }
    public boolean czyKtosPrzyKolejceSchodow(int i, boolean czyOdDolu) {
        //if (i < 0) {
            if (czyOdDolu)
                return ktosPrzyKolejceOdDolu[i];
            else
                return ktosPrzyKolejceOdGory[i];
        //}
        //return true;
    }
    public void setKtosPrzyKolejceSchodow(int i, boolean p, boolean czyOdDolu) {
        if (czyOdDolu)
            ktosPrzyKolejceOdDolu[i] = p;
        else
            ktosPrzyKolejceOdGory[i] = p;
    }

    public boolean czyKtosPrzyKolejceSchodowObustronnie(int i) {
        if (i < 0) {
            return ktosPrzyKolejceSchody[i];
        }
        return false;
    }
    public void setKtosPrzyKolejceSchodowObustronnie(int i, boolean p) {
        ktosPrzyKolejceSchody[i] = p;
    }

    public static void zwiekszLiczbaWKolejce(int i) {
        liczbaOsobDoKasy +=i;
    }

    public static int getLiczbaOsobDoKasy() {
        return liczbaOsobDoKasy;
    }

    public Para<Integer> zajmijKolejkeDoSchodow(boolean czyOdDolu) throws InterruptedException {
        /*int i = liczbaWKolejceDoSchodow;
        if (czyOdDolu) i = liczbaWKolejceDoSchodow++; //tymczasowe aby od gory nie leciały*/
            pobierzNumer.acquire();
                int i = liczbaWKolejceDoSchodow++;
                int j;
                if (czyOdDolu)
                    j =liczbaWKolejceDoSchodowOdDolu++;
                else
                    j =liczbaWKolejceDoSchodowOdGory++;
            pobierzNumer.release();
        return new Para(i, j);
    }

    public synchronized void zwolnijKolejkeDoSchodow(SwingWorkerKlienta k, boolean czyOdDolu) {
        k.numerWKolejceDoSchodow--;
        liczbaWKolejceDoSchodow--;
        ktosPrzyKolejceSchody[0] = false;
        if (czyOdDolu) {
            ktosPrzyKolejceOdDolu[0] = false;
            liczbaWKolejceDoSchodowOdDolu--;
        }
        else {
            ktosPrzyKolejceOdGory[0] = false;
            liczbaWKolejceDoSchodowOdGory--;
        }
        notifyAll(); //prawdopobnie zbedne bo usunalem monitor, ale puki co zostawiam (pod koniec usune jesli dziala bez tego)
    }

    /*
    public synchronized void czekajWKolejce(){  //źle zaimplementowałem i wymieniłem na semafory, nie uzywana (moze się wykorzysta pozniej)
        while(ktosSchodzi) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/

    public synchronized  void zwolnijKolejke(SwingWorkerKlienta k){
        liczbaOsobDoKasy--;
        ktosPrzyKolejceDoKasy[0] = false;
        //ktosSchodzi = false;
        notifyAll(); //prawdopobnie zbedne bo usunalem monitor, ale puki co zostawiam (pod koniec usune jesli dziala bez tego)
        k.numerWKolejce--;
    }



}
