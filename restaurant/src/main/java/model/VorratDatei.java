package model;


import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VorratDatei implements Vorrat {
    private static final String DATEINAME = "vorrat.txt";
    private static final String DATENVERZEICHNIS = "restaurant/data";

    private Map<String, Integer> vorratMap = new HashMap<>();
    private Map<String, String> produktZuKategorie = new HashMap<>();

    @Override
    public Map<String, Integer> getVorratMap() {
        return vorratMap;
    }

    @Override
    public Map<String, String> getProduktZuKategorie() {
       return produktZuKategorie;
    }

    @Override
    public void ladeVorrat() {
        File file = new File(DATENVERZEICHNIS, DATEINAME);
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

                        vorratMap.put(name, menge);
                        produktZuKategorie.put(name, kategorie);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("*** Fehler beim Lesen der Vorratsdatei: " + e.getMessage());
        }
    }

    @Override
    public void schreibeVorrat() {
        File dataOrdner = new File(DATENVERZEICHNIS);
        if (!dataOrdner.exists()) {
            dataOrdner.mkdirs();
        }

        File original = new File(dataOrdner, DATEINAME);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH-mm-ss"));
        File backup = new File(dataOrdner, timestamp + "_" + DATEINAME);

        try {
            if (original.exists()) {
                Files.copy(original.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                System.out.println("*** Es liegt noch keine " + DATEINAME + " vor – es wird eine neue erstellt.");
            }
        } catch (IOException e) {
            System.out.println("*** Fehler beim Erstellen des Backups: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(original))) {
            List<String> kategorienReihenfolge = List.of("Basisgericht", "Beilage", "Getraenk");

            for (String kategorie : kategorienReihenfolge) {
                vorratMap.keySet().stream()
                        .filter(name -> kategorie.equals(produktZuKategorie.get(name)))
                        .sorted()
                        .forEach(name -> {
                            int menge = vorratMap.getOrDefault(name, 0);
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
    
}
