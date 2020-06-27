package klasyokna;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static klasyokna.typPracownika.*;


//zawartość okna ala <body> w html
public class Kasyno extends JPanel {

    LinkedList<Pracownik> listaPracownikow = new LinkedList<>(); //bedzie dwoch
    LinkedList<Klient> listaKlientow = new LinkedList<>();
    LinkedList<SwingWorkerKlienta> ruszajacyKlienci = new LinkedList<>();
    ArrayList<MaszynyDoGry> listaKabin = new ArrayList<>();

    Kolejki kolejki;

    final BufferedImage background = ImageIO.read(this.getClass().getResource("/res/tlo2.png"));

    public Kasyno() throws IOException {
        listaPracownikow.add(new Pracownik(530, 605, kasjer));
        listaPracownikow.add(new Pracownik(325, 75, pokerzysta));//zabraklo czasu na dodanie stolika do pokera wiec tylko naprawia kabiny

        for (int i = 0; i<6; i++)
        {
            listaKabin.add(new MaszynyDoGry(150, 395 - i*55, zwrotKabiny.prawy));
        }
        for (int i = 0; i<6; i++)
        {
            listaKabin.add(new MaszynyDoGry(500, 395 - i*55, zwrotKabiny.lewy));
        }

        kolejki = new Kolejki(listaPracownikow.getFirst(), listaKabin);


        for(int i = 0; i < 10; i++)
        {
            listaKlientow.add(new Klient(5,750+(i*50), Color.DARK_GRAY,i*300));
            ruszajacyKlienci.add(new SwingWorkerKlienta(listaKlientow.get(i), kolejki, i));
        }
        SwingWorkerPracownik kasjer = new SwingWorkerPracownik(listaPracownikow.get(0), kolejki);
        SwingWorkerPracownik mechanik = new SwingWorkerPracownik(listaPracownikow.get(1), kolejki);



        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kasyno.this.repaint(); //wywołuje paintComponent
            }
        });
        timer.start(); // licznik które wywołuje odświeżanie stanu w oknie

        kasjer.execute();
        mechanik.execute();
        for (SwingWorkerKlienta s : ruszajacyKlienci)
        {
            s.execute();
        }


        Timer timer2 = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < 10; i++)
                {
                    System.out.println("Czy wyszedł klient nr " + i + "? - " + ruszajacyKlienci.get(i).isDone());
                    if (ruszajacyKlienci.get(i).isDone())
                    {
                        listaKlientow.set(i , new Klient(5,700+(i*50), Color.DARK_GRAY,i*300));
                        ruszajacyKlienci.set(i, new SwingWorkerKlienta(listaKlientow.get(i), kolejki, i));
                        ruszajacyKlienci.get(i).execute();
                    }
                }
            }
        });
        timer2.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 700, 700, null);
        Graphics2D g2d = (Graphics2D)g;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(500,600,25,50); //kasa
        g.fillRect(625,500, 25,100); //magazyn z monetami dla pracowników

        for (Pracownik p : listaPracownikow)
        {
            g.setColor(p.getColor());
            int x = p.getX();
            int y = p.getY();
            int d = p.getDiameter();
            Ellipse2D.Double circle = new Ellipse2D.Double(x,y,d,d);
            g2d.fill(circle);
            if (p.getCoins()!=0) {
                g.setColor(Color.BLACK);
                g2d.drawString(Integer.toString(p.getCoins()), x + 15, y + 25);
            }
        }

        for (Klient k : listaKlientow)
        {
            g.setColor(k.getColor());
            int x = k.getX();
            int y = k.getY();
            int d = k.getDiameter();
            Ellipse2D.Double circle = new Ellipse2D.Double(x,y,d,d);
            g2d.fill(circle);
            if (k.getCoins()!=0) {
                g.setColor(Color.BLACK);
                g2d.drawString(Integer.toString(k.getCoins()), x + 15, y + 25);
            }
        }
        for (MaszynyDoGry m : listaKabin)
        {
            g.setColor(m.getColor());
            int x = m.getX();
            int y = m.getY();
            int d = m.getDiameter();
            g.fillRect(x,y,d,d);
        }
    }
}
