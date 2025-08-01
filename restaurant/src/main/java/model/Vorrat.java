package model;

import java.util.Map;

public interface Vorrat {
    Map<String, Integer> getVorratMap();

    Map<String, String> getProduktZuKategorie();

    Map<String, Double> getProduktPreise();

    void ladeVorrat();

    void schreibeVorrat();

    /**
     * Gibt den Vorrat als formatierte String-Repräsentation zurück.
     * @return String-Repräsentation des Vorrats
     */
    default String getVorratAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========\n");
        sb.append("Vorrat\n");
        sb.append("========\n\n");

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
                    sb.append(kategorie).append("\n");
                    kategorien.get(kategorie).stream()
                            .sorted()
                            .forEach(name -> {
                                int menge = vorrat.getOrDefault(name, 0);
                                double preis = produktPreise.getOrDefault(name, 0.0);
                                sb.append(String.format("- %-18s %3d Stück  %6.2f €%n", name + ":", menge, preis));
                            });
                    sb.append("\n");
                });
        return sb.toString();
    }

    // Die bestehende Methode für die Konsolenausgabe
    default void zeigeVorrat() {
        System.out.println(getVorratAsString());
    }
}