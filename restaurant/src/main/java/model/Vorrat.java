package model;

import java.util.Map;

public interface Vorrat {
    Map<String, Integer> getVorratMap();

    Map<String, String> getProduktZuKategorie();

    Map<String, Double> getProduktPreise();

    void ladeVorrat();

    void schreibeVorrat();

    default void zeigeVorrat() {
        System.out.println("========");
        System.out.println("Vorrat");
        System.out.println("========\n");

        Map<String, Integer> vorrat = getVorratMap();
        Map<String, String> produktZuKategorie = getProduktZuKategorie();
        Map<String, Double> produktPreise = getProduktPreise();
        Map<String, java.util.List<String>> kategorien = new java.util.HashMap<>();

        for (String produkt : vorrat.keySet()) {
            String kategorie = produktZuKategorie.getOrDefault(produkt, "Sonstige");
            kategorien.computeIfAbsent(kategorie, k -> new java.util.ArrayList<>()).add(produkt);
        }

        kategorien.keySet().stream()
                .sorted()
                .forEach(kategorie -> {
                    System.out.println(kategorie);
                    kategorien.get(kategorie).stream()
                            .sorted()
                            .forEach(name -> {
                                int menge = vorrat.getOrDefault(name, 0);
                                double preis = produktPreise.getOrDefault(name, 0.0);
                                System.out.printf("- %-18s %3d Stück  %6.2f €%n", name + ":", menge, preis);
                            });
                    System.out.println();
                });
    }
}
