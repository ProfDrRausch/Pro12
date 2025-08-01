package controller;

import model.Produkt;
import model.ProduktFabrik;
import model.Vorrat;
import model.Warenkorb;

/**
 * Dieser universelle MenuHandler dient als Controller für die Anwendung.
 * Er enthält die Geschäftslogik, die von jeder Benutzeroberfläche wiederverwendet werden kann.
 * Er hat keine direkte Abhängigkeit von Scanner oder System.out.
 */
public class MenuHandler {
    private Warenkorb warenkorb;
    private Vorrat vorrat;
    private final ProduktFabrik produktFabrik;

    public MenuHandler(Vorrat vorrat, Warenkorb warenkorb) {
        this.vorrat = vorrat;
        this.warenkorb = warenkorb;
        this.produktFabrik = new ProduktFabrik(vorrat);
    }
    
    // Methoden, die die Geschäftslogik ausführen und ein Ergebnis zurückgeben

    public boolean waehleBasisgericht(String eingabe) {
        Produkt gericht = produktFabrik.erstelleBasisgericht(eingabe);
        if (gericht != null) {
            warenkorb.setBasisgericht(gericht);
            return true;
        }
        return false;
    }

    public boolean beilageHinzufuegen(String eingabe) {
        Produkt beilage = produktFabrik.erstelleBeilage(eingabe);
        if (beilage != null) {
            warenkorb.addBeilage(beilage);
            return true;
        }
        return false;
    }

    public boolean beilageEntfernen(int index) {
        if (index >= 0 && index < warenkorb.getBeilagen().size()) {
            warenkorb.removeBeilage(index);
            return true;
        }
        return false;
    }
    
    public boolean getraenkWaehlen(String eingabe) {
        Produkt getraenk = produktFabrik.erstelleGetraenk(eingabe);
        if (getraenk != null) {
            warenkorb.setGetraenk(getraenk);
            return true;
        }
        return false;
    }

    public void bestellungAbschliessen() {
        vorrat.schreibeVorrat();
    }
}