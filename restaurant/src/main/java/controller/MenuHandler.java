package controller;

import java.util.Scanner;

import model.Produkt;
import model.ProduktFabrik;
import model.Warenkorb;

public class MenuHandler {
    private Warenkorb warenkorb;
    private Scanner scanner = new Scanner(System.in);

    public MenuHandler(Warenkorb warenkorb) {
        this.warenkorb = warenkorb;
    }

    public void start() {
        boolean running = true;
        while (running) {
            zeigeKopfzeile();
            warenkorb.druckeWarenkorb();
            zeigeMenue();

            String eingabe = scanner.nextLine().trim();

            switch (eingabe) {
                case "1n" -> basisgerichtWaehlen();
                case "1d" -> warenkorb.removeBasisgericht();
                case "2n" -> beilageHinzufuegen();
                case "2d" -> beilageEntfernen();
                case "3a" -> getraenkWaehlen();
                case "3d" -> warenkorb.removeGetraenk();
                case "9" -> warenkorb.zeigeVorrat();
                case "0" -> {
                    warenkorb.schreibeVorratInDatei();
                    System.out.println("\nIhre endgültige Bestellung:");
                    warenkorb.druckeWarenkorb();
                    System.out.println("Vielen Dank - guten Appetit! 🍽️");
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

    private void basisgerichtWaehlen() {
        System.out.println("Wählen Sie ein Basisgericht:");
        System.out.println("1: Garnelen | 2: Hueftsteak | 3: Tofu | 4: Wienerschnitzel");
        String eingabe = scanner.nextLine().trim();

        Produkt gericht = ProduktFabrik.erstelleBasisgericht(eingabe);
        if (gericht != null) {
            warenkorb.setBasisgericht(gericht);
        } else {
            System.out.println("*** Ungültige Eingabe.");
        }
    }

    private void beilageHinzufuegen() {
        System.out.println("Wählen Sie eine Beilage:");
        System.out.println("1: Pommes | 2: Bratkartoffeln | 3: Nudeln | 4: Salat | 5: Suppe");

        String eingabe = scanner.nextLine().trim();
        Produkt beilage = ProduktFabrik.erstelleBeilage(eingabe);

        if (beilage != null) {
            warenkorb.addBeilage(beilage);
        } else {
            System.out.println("*** Ungültige Eingabe.");
        }
    }

    private void beilageEntfernen() {
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
            warenkorb.removeBeilage(index);
        } catch (Exception e) {
            System.out.println("*** Fehlerhafte Eingabe.");
        }
    }

    private void getraenkWaehlen() {
        System.out.println("Wählen Sie ein Getränk:");
        System.out.println("1: Wasser | 2: Bier | 3: Wein");

        String eingabe = scanner.nextLine().trim();
        Produkt getraenk = ProduktFabrik.erstelleGetraenk(eingabe);

        if (getraenk != null) {
            warenkorb.setGetraenk(getraenk);
        } else {
            System.out.println("*** Ungültige Eingabe.");
        }
    }

}
