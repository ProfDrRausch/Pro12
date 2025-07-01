package model;

public class ProduktFabrik {

    private final Vorrat vorrat;

    public ProduktFabrik(Vorrat vorrat) {
        this.vorrat = vorrat;
    }

    public Produkt erstelleBasisgericht(String eingabe) {
        return switch (eingabe) {
            case "1" -> new Garnelen(preis("Garnelen"));
            case "2" -> new Hueftsteak(preis("Hueftsteak"));
            case "3" -> new Tofu(preis("Tofu"));
            case "4" -> new Wienerschnitzel(preis("Wienerschnitzel"));
            default -> null;
        };
    }

    public Produkt erstelleBeilage(String eingabe) {
        return switch (eingabe) {
            case "1" -> new Pommes(preis("Pommes"));
            case "2" -> new Bratkartoffeln(preis("Bratkartoffeln"));
            case "3" -> new Nudeln(preis("Nudeln"));
            case "4" -> new Salat(preis("Salat"));
            case "5" -> new Suppe(preis("Suppe"));
            default -> null;
        };
    }

    public Produkt erstelleGetraenk(String eingabe) {
        return switch (eingabe) {
            case "1" -> new Wasser(preis("Wasser"));
            case "2" -> new Bier(preis("Bier"));
            case "3" -> new Wein(preis("Wein"));
            default -> null;
        };
    }

    private double preis(String name) {
        return vorrat.getProduktPreise().getOrDefault(name, 0.0);
    }
}
