package controller;

import model.Vorrat;
import model.VorratDB;
import model.Warenkorb;

public class MainKonsole {
    public static void main(String[] args) {
        // Vorrat vorrat = new VorratDatei();
        // Vorrat vorrat = new VorratXML();
        Vorrat vorrat = new VorratDB();
        vorrat.ladeVorrat();
        
        Warenkorb warenkorb = new Warenkorb(vorrat);
        
        // 1. Instanziieren des universellen Controllers
        MenuHandler menuHandler = new MenuHandler(vorrat, warenkorb);
        
        // 2. Instanziieren des konsolenspezifischen Handlers und Starten des Menüs
        // Dieser Handler übernimmt die Konsolen-Logik und nutzt den MenuHandler für die Geschäftslogik
        KonsolenHandler konsolenHandler = new KonsolenHandler(menuHandler, warenkorb, vorrat);
        konsolenHandler.startKonsolenMenu();
        
        // Nach dem Beenden des Menüs wird das Endergebnis gedruckt.
        System.out.println("\nIhre endgültige Bestellung:");
        warenkorb.druckeWarenkorb();
        System.out.println("Vielen Dank - guten Appetit! 🍽️");
    }
}