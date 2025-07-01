package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.FormatterUtil;

public class Warenkorb {
    private Produkt basisgericht;
    private Produkt getraenk;
    private List<Produkt> beilagen;

    private Map<String, Integer> vorratMap = null;// = new HashMap<>();
  
    public Warenkorb(Vorrat vorrat  ) {
        beilagen = new ArrayList<>();
        this.vorratMap = vorrat.getVorratMap();
    }

    public Produkt getBasisgericht() {
        return basisgericht;
    }

    public void setBasisgericht(Produkt basisgericht) {
        removeBasisgericht(); // Vorrat zurückbuchen, falls vorhanden

        if (vorratMap.getOrDefault(basisgericht.getName(), 0) > 0) {
            this.basisgericht = basisgericht;
            vorratMap.put(basisgericht.getName(), vorratMap.get(basisgericht.getName()) - 1);
        } else {
            System.out.println("*** " + basisgericht.getName() + " ist nicht mehr verfügbar!");
        }
    }

    public void removeBasisgericht() {
        if (this.basisgericht != null) {
            String name = this.basisgericht.getName();
            vorratMap.put(name, vorratMap.getOrDefault(name, 0) + 1);
            this.basisgericht = null;
        }
    }

    public Produkt getGetraenk() {
        return getraenk;
    }

    public void setGetraenk(Produkt getraenk) {
        removeGetraenk(); // Vorrat zurückbuchen, falls bereits ein Getränk gesetzt ist

        if (vorratMap.getOrDefault(getraenk.getName(), 0) > 0) {
            this.getraenk = getraenk;
            vorratMap.put(getraenk.getName(), vorratMap.get(getraenk.getName()) - 1);
        } else {
            System.out.println("*** " + getraenk.getName() + " ist nicht mehr verfügbar!");
        }
    }

    public void removeGetraenk() {
        if (this.getraenk != null) {
            String name = this.getraenk.getName();
            vorratMap.put(name, vorratMap.getOrDefault(name, 0) + 1);
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
        if (vorratMap.getOrDefault(name, 0) > 0) {
            beilagen.add(beilage);
            vorratMap.put(name, vorratMap.get(name) - 1);
        } else {
            System.out.println("*** " + name + " ist nicht mehr verfügbar!");
        }
    }

    public void removeBeilage(Produkt beilage) {
        if (beilage != null && beilagen.remove(beilage)) {
            String name = beilage.getName();
            vorratMap.put(name, vorratMap.getOrDefault(name, 0) + 1);
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
}
