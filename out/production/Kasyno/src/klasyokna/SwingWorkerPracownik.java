package klasyokna;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class SwingWorkerPracownik extends SwingWorker<Object,Object> {

    Pracownik pracownik;
    Kolejki kolejki;
    //int numerWKolejce;

    public SwingWorkerPracownik(Pracownik pracownik, Kolejki kolejki){
        this.pracownik = pracownik;
        this.kolejki = kolejki;
    }

    private void czekaj() throws InterruptedException {
        Thread.sleep(15) ;
    }
    private void czekaj(long time) throws InterruptedException {
        Thread.sleep(time) ;
    }
    public void ruszDo(Integer celX, Integer celY) throws InterruptedException {
        if(celX != null) {
            while (pracownik.getX() != celX) {
                if (pracownik.getX() > celX)
                    pracownik.move(-1, 0);
                else
                    pracownik.move(1, 0);
                czekaj();
            }
        }
        if(celY != null) {
            while (pracownik.getY() != celY) {
                if (pracownik.getY() > celY)
                    pracownik.move(0, -1);
                else
                    pracownik.move(0, 1);
                czekaj();
            }
        }
    }

    @Override
    protected Object doInBackground() throws Exception {
        if (pracownik.getTyp() == typPracownika.kasjer)
        {
            czekaj(400);
            for(;;)
            {
                try{
                    kolejki.przyniesDoKasy.acquire();
                    if(pracownik.getCoins() == 0)
                    {

                        ruszDo(575,550);
                        //czekaj(200);
                        for (int i = 0 ; i<=10; i++) {
                            czekaj(400);
                            pracownik.setCoins(i);
                        }
                        ruszDo(pracownik.getStartX(),pracownik.getStartY());
                    }
                    czekaj(500);
                    kolejki.kup.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (pracownik.getTyp() == typPracownika.pokerzysta)
        {
            czekaj(400);
            Iterator<MaszynyDoGry> iter;
            //MaszynyDoGry p;
            for(;;)
            {
                iter = kolejki.getListaMaszyn().iterator();
                sprawdzINaprawKolumneMachin(iter);
                ruszDo(null, pracownik.getStartY());
                ruszDo(pracownik.getStartX(),null);
                sprawdzINaprawKolumneMachin(iter);
                ruszDo(null, pracownik.getStartY());
                czekaj(100);
            }
        }
        return null;
    }

    private void sprawdzINaprawKolumneMachin(Iterator<MaszynyDoGry> iter) throws InterruptedException {
        MaszynyDoGry p;
        for (int i = 0; i < (kolejki.getListaMaszyn().size()/2) ; i++)
        {
            p = iter.next();
            if(!p.czyAktywny)
            {
                Para<Integer> pozycjaNaprawiania = p.getPozycjaNaprawiania();
                int x = pozycjaNaprawiania.getX();
                int y = pozycjaNaprawiania.getY();
                ruszDo(x,y);
                czekaj(200);
                p.czyAktywny = true;
                p.setColor(Color.GRAY);
                czekaj(100);
            }
        }
    }
}
