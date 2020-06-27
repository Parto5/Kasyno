package klasyokna;

import java.awt.*;

//dziedzicza go klient, pracownik i maszyny do grania
public abstract class Obiekt {

    protected int x;
    protected int y;
    protected int diameter = 40;
    protected Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getY() {
        return y;
    }

    public int getDiameter() {
        return diameter;
    }

    public int getX() {
        return x;
    }
}
