package controller;

import javax.swing.SwingUtilities;

import model.Vorrat;
import model.VorratDB;
import model.Warenkorb;
import view.RestaurantGUI;

public class MainGui {
    public static void main(String[] args) {
        Vorrat vorrat = new VorratDB();
        vorrat.ladeVorrat();
        Warenkorb warenkorb = new Warenkorb(vorrat);
        
        MenuHandler menuHandler = new MenuHandler(vorrat, warenkorb);
        
        SwingUtilities.invokeLater(() -> {
            // Hier wird das Vorrat-Objekt an den Konstruktor der GUI übergeben.
            RestaurantGUI gui = new RestaurantGUI(menuHandler, warenkorb, vorrat);
            gui.setVisible(true);
        });
    }
}