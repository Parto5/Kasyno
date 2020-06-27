package klasyokna;

import java.awt.*;
//Klient i Pracownik mają sporo podobnych danych to mozna zrobic wspolna klase abstrakcyjna do dziedziczenia  - zrobilem obiekt do tego
public class Klient extends Obiekt{
    private int initialSleep;
    private int coins;  //poczatkowy zasob żetonów
    private int goal;  //stan zasobow wygranej po której przerwie gre
    private final int loss;  //stan zasobow przegranej po ktorej przerwie gre


    public Klient(int x, int y, Color color, int sleep)
    {
        super.x = x;
        super.y = y;
        super.diameter = 40;
        super.color = color;
        this.initialSleep = sleep;
        coins = 0;
        goal = 15;
        loss = 0;  //można dodać losowość dla tych wartosci
    }


    public int getCoins() {
        return coins;
    }

    public void changeCoins(int coins) {
        this.coins += coins;
    }
    public void move(int dx, int dy)
    {
        x += dx;
        y += dy;
    }

    public int getInitialSleep() {
        return initialSleep;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getLoss() {
        return loss;
    }
}
