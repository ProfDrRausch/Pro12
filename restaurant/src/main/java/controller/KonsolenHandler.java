package controller;

import java.util.Scanner;
import model.Warenkorb;
import model.Vorrat;

/**
 * Diese Klasse ist der Controller für die Konsolen-Benutzeroberfläche.
 * Sie enthält den Menü-Loop und die Interaktion mit dem Benutzer über Scanner.
 */
public class KonsolenHandler {
    private final MenuHandler menuHandler;
    private final Warenkorb warenkorb;
    private final Vorrat vorrat;
    private Scanner scanner = new Scanner(System.in);

    public KonsolenHandler(MenuHandler menuHandler, Warenkorb warenkorb, Vorrat vorrat) {
        this.menuHandler = menuHandler;
        this.warenkorb = warenkorb;
        this.vorrat = vorrat;
    }
    
    public void startKonsolenMenu() {
        boolean running = true;
        while (running) {
            zeigeKopfzeile();
            warenkorb.druckeWarenkorb();
            zeigeMenue();

            String eingabe = scanner.nextLine().trim();

            switch (eingabe) {
                case "1n" -> waehleBasisgerichtKonsole();
                case "1d" -> warenkorb.removeBasisgericht();
                case "2n" -> beilageHinzufuegenKonsole();
                case "2d" -> beilageEntfernenKonsole();
                case "3a" -> getraenkWaehlenKonsole();
                case "3d" -> warenkorb.removeGetraenk();
                case "9" -> vorrat.zeigeVorrat();
                case "0" -> {
                    menuHandler.bestellungAbschliessen();
                    running = false;
                }
                default -> System.out.println("*** Ungültige Eingabe.");
            }
        }
    }

    private void zeigeKopfzeile() {
        System.out.println("\n\n\n=================================");
        System.out.println("HFT-Restaurant");
        System.out.println("=================================\n");
    }

    private void zeigeMenue() {
        System.out.println("Was möchten Sie tun?");
        System.out.println("1n: Basisgericht wählen");
        System.out.println("1d: Basisgericht löschen");
        System.out.println("2n: Beilage hinzufügen");
        System.out.println("2d: Beilage entfernen");
        System.out.println("3a: Getränk wählen");
        System.out.println("3d: Getränk löschen");
        System.out.println("9 : Vorrat anzeigen");
        System.out.println("0 : Bestellung abschließen");
    }

    private void waehleBasisgerichtKonsole() {
        System.out.println("Wählen Sie ein Basisgericht:");
        System.out.println("1: Garnelen | 2: Hueftsteak | 3: Tofu | 4: Wienerschnitzel");
        String eingabe = scanner.nextLine().trim();
        menuHandler.waehleBasisgericht(eingabe); // Delegierung an den MenuHandler
    }

    private void beilageHinzufuegenKonsole() {
        System.out.println("Wählen Sie eine Beilage:");
        System.out.println("1: Pommes | 2: Bratkartoffeln | 3: Nudeln | 4: Salat | 5: Suppe");
        String eingabe = scanner.nextLine().trim();
        menuHandler.beilageHinzufuegen(eingabe); // Delegierung an den MenuHandler
    }

    private void beilageEntfernenKonsole() {
        if (warenkorb.getBeilagen().isEmpty()) {
            System.out.println("*** Es sind keine Beilagen vorhanden.");
            return;
        }
        System.out.println("Welche Beilage möchten Sie entfernen?");
        for (int i = 0; i < warenkorb.getBeilagen().size(); i++) {
            System.out.println((i + 1) + ": " + warenkorb.getBeilagen().get(i).getClass().getSimpleName());
        }
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            menuHandler.beilageEntfernen(index); // Delegierung an den MenuHandler
        } catch (Exception e) {
            System.out.println("*** Fehlerhafte Eingabe.");
        }
    }

    private void getraenkWaehlenKonsole() {
        System.out.println("Wählen Sie ein Getränk:");
        System.out.println("1: Wasser | 2: Bier | 3: Wein");
        String eingabe = scanner.nextLine().trim();
        menuHandler.getraenkWaehlen(eingabe); // Delegierung an den MenuHandler
    }
}