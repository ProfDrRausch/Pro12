package model;

public class ProduktFabrik {

    public static Produkt erstelleBasisgericht(String eingabe) {
        return switch (eingabe) {
            case "1" -> new Garnelen();
            case "2" -> new Hueftsteak();
            case "3" -> new Tofu();
            case "4" -> new Wienerschnitzel();
            default -> null;
        };
    }

    public static Produkt erstelleBeilage(String eingabe) {
        return switch (eingabe) {
            case "1" -> new Pommes();
            case "2" -> new Bratkartoffeln();
            case "3" -> new Nudeln();
            case "4" -> new Salat();
            case "5" -> new Suppe();
            default -> null;
        };
    }

    public static Produkt erstelleGetraenk(String eingabe) {
        return switch (eingabe) {
            case "1" -> new Wasser();
            case "2" -> new Bier();
            case "3" -> new Wein();
            default -> null;
        };
    }
}
