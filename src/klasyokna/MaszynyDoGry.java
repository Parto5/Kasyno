package klasyokna;

import java.awt.*;
import klasyokna.zwrotKabiny;

import static klasyokna.zwrotKabiny.prawy;

public class MaszynyDoGry extends Obiekt {

    private final zwrotKabiny zwrotKabiny;
    private int pozycjaGraniaX;
    private int pozycjaGraniaY;

    private int pozycjaNaprawianiaX;
    private int pozycjaNaprawianiaY;
    boolean czyWolny = true;
    boolean czyAktywny = false;

    public MaszynyDoGry(int x, int y, zwrotKabiny zwrot) {
        super.x = x;
        super.y = y;
        super.diameter = 50;
        super.color = Color.RED;
        zwrotKabiny = zwrot;
        pozycjaGraniaY = y + 5;
        pozycjaNaprawianiaY = y + 5;
        if (zwrotKabiny == prawy) {
            pozycjaGraniaX = x + 55;
            pozycjaNaprawianiaX = x - 45;
        }
        else
        {
            pozycjaGraniaX = x - 45;
            pozycjaNaprawianiaX = x + 55;
        }

    }

    public klasyokna.zwrotKabiny getZwrotKabiny() {
        return zwrotKabiny;
    }

    public Para<Integer> getPozycjaNaprawiania() {
        return new Para(pozycjaNaprawianiaX, pozycjaNaprawianiaY);
    }
    public Para<Integer> getPozycjaGrania() {
        return new Para(pozycjaGraniaX, pozycjaGraniaY);
    }

    public void zwalniam() {
        czyAktywny = false;
        color = Color.RED;
        czyWolny = true;
    }
}
