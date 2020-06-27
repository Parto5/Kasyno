import klasyokna.Okno;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Okno okno = new Okno("Sala Kasyna");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
