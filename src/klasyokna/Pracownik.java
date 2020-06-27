package klasyokna;

import java.awt.*;

//Klient i Pracownik mają sporo podobnych danych to mozna zrobic wspolna klase abstrakcyjna do dziedziczenia - zrobilem obiekt do tego
public class Pracownik extends Obiekt{

    private final int startX;
    private final int startY;
    private final Color color;
    private final typPracownika typ;
    private int coins;



    public Pracownik(int x, int y, typPracownika typpracownika)
    {
        super.x = x;
        super.y = y;
        startX = x;
        startY = y;
        coins = 0;
        this.color = Color.MAGENTA;
        this.typ = typpracownika;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Color getColor() {
        return color;
    }

    public void move(int dx, int dy)
    {
        x += dx;
        y += dy;
    }

    public void transakcja(int coinsy)
    {
        coins += coinsy;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public typPracownika getTyp() {
        return typ;
    }

}
