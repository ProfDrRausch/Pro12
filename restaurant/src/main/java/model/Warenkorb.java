package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.FormatterUtil;

public class Warenkorb {
    private Produkt basisgericht;
    private Produkt getraenk;
    private List<Produkt> beilagen;

    private Map<String, Integer> vorrat = new HashMap<>();

    public Warenkorb() {
        beilagen = new ArrayList<>();
    }

    public Produkt getBasisgericht() {
        return basisgericht;
    }

    public void setBasisgericht(Produkt basisgericht) {
        removeBasisgericht(); // Vorrat zurückbuchen, falls vorhanden

        if (vorrat.getOrDefault(basisgericht.getName(), 0) > 0) {
            this.basisgericht = basisgericht;
            vorrat.put(basisgericht.getName(), vorrat.get(basisgericht.getName()) - 1);
        } else {
            System.out.println("*** " + basisgericht.getName() + " ist nicht mehr verfügbar!");
        }
    }

    public void removeBasisgericht() {
        if (this.basisgericht != null) {
            String name = this.basisgericht.getName();
            vorrat.put(name, vorrat.getOrDefault(name, 0) + 1);
            this.basisgericht = null;
        }
    }

    public Produkt getGetraenk() {
        return getraenk;
    }

    public void setGetraenk(Produkt getraenk) {
        removeGetraenk(); // Vorrat zurückbuchen, falls bereits ein Getränk gesetzt ist

        if (vorrat.getOrDefault(getraenk.getName(), 0) > 0) {
            this.getraenk = getraenk;
            vorrat.put(getraenk.getName(), vorrat.get(getraenk.getName()) - 1);
        } else {
            System.out.println("*** " + getraenk.getName() + " ist nicht mehr verfügbar!");
        }
    }

    public void removeGetraenk() {
        if (this.getraenk != null) {
            String name = this.getraenk.getName();
            vorrat.put(name, vorrat.getOrDefault(name, 0) + 1);
            this.getraenk = null;
        }
    }

    public List<Produkt> getBeilagen() {
        return beilagen;
    }

    public void addBeilage(Produkt beilage) {
        String name = beilage.getName();

        // Überprüfung auf Duplikate
        // for (Produkt vorhandene : beilagen) {
        // if (vorhandene.getName().equalsIgnoreCase(name)) {
        // System.out.println("*** " + name + " wurde bereits hinzugefügt!");
        // return;
        // }
        // }

        // Duplikatsprüfung via Stream
        boolean schonVorhanden = beilagen.stream()
                .anyMatch(b -> b.getName().equalsIgnoreCase(name));

        if (schonVorhanden) {
            System.out.println("*** " + name + " wurde bereits hinzugefügt!");
            return;
        }

        // Vorratsprüfung und hinzufügen
        if (vorrat.getOrDefault(name, 0) > 0) {
            beilagen.add(beilage);
            vorrat.put(name, vorrat.get(name) - 1);
        } else {
            System.out.println("*** " + name + " ist nicht mehr verfügbar!");
        }
    }

    public void removeBeilage(Produkt beilage) {
        if (beilage != null && beilagen.remove(beilage)) {
            String name = beilage.getName();
            vorrat.put(name, vorrat.getOrDefault(name, 0) + 1);
        }
    }

    public Produkt removeBeilage(int index) {
        if (index >= 0 && index < beilagen.size()) {
            Produkt entfernt = beilagen.get(index);
            removeBeilage(entfernt); // kümmert sich um Vorrat
            return entfernt;
        }
        return null;
    }

    public double getGesamtpreis() {
        double summe = 0.0;
        if (basisgericht != null)
            summe += basisgericht.getPreis();
        if (getraenk != null)
            summe += getraenk.getPreis();
        for (Produkt b : beilagen) {
            summe += b.getPreis();
        }
        return summe;
    }

    public void druckeWarenkorb() {
        final int breite1 = 20; // Kategorie
        final int breite2 = 25; // Beschreibung
        final int breite3 = 12; // Preis inkl. € + |
        final int trennBreite = 3; // " | "
        final int gesamtbreite = breite1 + breite2 + breite3 + 2 * trennBreite + 1;

        String titel = "===== Ihr Warenkorb ";
        String kopfzeile = titel + FormatterUtil.trennlinie(Math.max(0, gesamtbreite - titel.length()), '=');
        System.out.println(kopfzeile);

        // Tabellenkopf
        System.out.println(
                FormatterUtil.zelleLinks("", breite1) + " | " +
                        FormatterUtil.zelleLinks("", breite2) + " | " +
                        FormatterUtil.zelleRechts("Preis", breite3 - 3) + " € |");
        System.out.println(FormatterUtil.trennlinie(gesamtbreite, '-'));

        double gesamtpreis = 0.0;

        // Basisgericht
        if (basisgericht != null) {
            System.out.println(
                    FormatterUtil.zelleLinks("Basisgericht", breite1) + " | " +
                            FormatterUtil.zelleLinks(basisgericht.getName(), breite2) + " | " +
                            FormatterUtil.preisMitEuro(basisgericht.getPreis(), breite3) + " |");
            gesamtpreis += basisgericht.getPreis();
        } else {
            System.out.println(
                    FormatterUtil.zelleLinks("Basisgericht", breite1) + " | " +
                            FormatterUtil.zelleLinks("---", breite2) + " | " +
                            FormatterUtil.zelleRechts("", breite3 - 3) + " € |");
        }

        // Beilagen
        if (beilagen.isEmpty()) {
            System.out.println(
                    FormatterUtil.zelleLinks("Beilagen", breite1) + " | " +
                            FormatterUtil.zelleLinks("---", breite2) + " | " +
                            FormatterUtil.zelleRechts("", breite3 - 3) + " € |");
        } else {
            for (Produkt b : beilagen) {
                System.out.println(
                        FormatterUtil.zelleLinks("Beilage", breite1) + " | " +
                                FormatterUtil.zelleLinks(b.getName(), breite2) + " | " +
                                FormatterUtil.preisMitEuro(b.getPreis(), breite3) + " |");
                gesamtpreis += b.getPreis();
            }
        }

        // Getränk
        if (getraenk != null) {
            System.out.println(
                    FormatterUtil.zelleLinks("Getränk", breite1) + " | " +
                            FormatterUtil.zelleLinks(getraenk.getName(), breite2) + " | " +
                            FormatterUtil.preisMitEuro(getraenk.getPreis(), breite3) + " |");
            gesamtpreis += getraenk.getPreis();
        } else {
            System.out.println(
                    FormatterUtil.zelleLinks("Getränk", breite1) + " | " +
                            FormatterUtil.zelleLinks("---", breite2) + " | " +
                            FormatterUtil.zelleRechts("", breite3 - 3) + " € |");
        }

        // Abschluss
        System.out.println(FormatterUtil.trennlinie(gesamtbreite, '-'));
        System.out.println(
                FormatterUtil.zelleLinks("Summe", breite1 + breite2 + trennBreite) + " | " +
                        FormatterUtil.preisMitEuro(gesamtpreis, breite3) + " |");
        System.out.println(FormatterUtil.trennlinie(gesamtbreite, '=') + "\n");
    }

    private Map<String, String> produktZuKategorie = new HashMap<>();

    public void ladeVorrat(String dateiname) {
        File file = new File("restaurant/data", dateiname);
        if (!file.exists()) {
            System.out.println("*** Datei " + file.getPath() + " nicht gefunden!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teile = zeile.split("=");
                if (teile.length == 2) {
                    String[] kategorieUndName = teile[0].split(":");
                    if (kategorieUndName.length == 2) {
                        String kategorie = kategorieUndName[0].trim();
                        String name = kategorieUndName[1].trim();
                        int menge = Integer.parseInt(teile[1].trim());

                        vorrat.put(name, menge);
                        produktZuKategorie.put(name, kategorie);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("*** Fehler beim Lesen der Vorratsdatei: " + e.getMessage());
        }
    }

    public void schreibeVorratInDatei() {
        File dataOrdner = new File("restaurant/data");
        if (!dataOrdner.exists()) {
            dataOrdner.mkdirs();
        }

        File original = new File(dataOrdner, "vorrat.txt");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH-mm-ss"));
        File backup = new File(dataOrdner, timestamp + "_vorrat.txt");

        // Backup anlegen
        try {
            if (original.exists()) {
                Files.copy(original.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                System.out.println("*** Es liegt noch keine vorrat.txt vor – es wird eine neue erstellt.");
            }
        } catch (IOException e) {
            System.out.println("*** Fehler beim Erstellen des Backups: " + e.getMessage());
            return;
        }

        // Vorrat geordnet schreiben: zuerst Basisgericht, dann Beilage, dann Getraenk
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(original))) {
            List<String> kategorienReihenfolge = List.of("Basisgericht", "Beilage", "Getraenk");

            for (String kategorie : kategorienReihenfolge) {
                vorrat.keySet().stream()
                        .filter(name -> kategorie.equals(produktZuKategorie.get(name)))
                        .sorted()
                        .forEach(name -> {
                            int menge = vorrat.getOrDefault(name, 0);
                            try {
                                writer.write(kategorie + ":" + name + "=" + menge);
                                writer.newLine();
                            } catch (IOException e) {
                                System.out.println("*** Fehler beim Schreiben von " + name + ": " + e.getMessage());
                            }
                        });
            }

        } catch (IOException e) {
            System.out.println("*** Fehler beim Schreiben der Vorratsdatei: " + e.getMessage());
        }
    }

    public void zeigeVorrat() {
        System.out.println("========");
        System.out.println("Vorrat");
        System.out.println("========\n");

        Map<String, List<String>> kategorien = new HashMap<>();

        for (String produkt : vorrat.keySet()) {
            String kategorie = produktZuKategorie.getOrDefault(produkt, "Sonstige");
            kategorien.computeIfAbsent(kategorie, k -> new ArrayList<>()).add(produkt);
        }

        kategorien.keySet().stream()
                .sorted()
                .forEach(kategorie -> {
                    System.out.println(kategorie);
                    kategorien.get(kategorie).stream()
                            .sorted()
                            .forEach(name -> {
                                int menge = vorrat.getOrDefault(name, 0);
                                System.out.printf("- %-18s%4d%n", name + ":", menge);
                            });
                    System.out.println();
                });
    }

}
