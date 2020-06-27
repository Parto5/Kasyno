package klasyokna;

import javax.swing.*;
import java.io.IOException;

//okno aplikacji
public class Okno extends JFrame {
    public Okno (String title) throws IOException {
        super(title);
        setSize(710, 730);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // aby program się wyłączał po zamknięciu okna
        add(new Kasyno()); //dodanie zawartości
        setVisible(true);
    }
}
