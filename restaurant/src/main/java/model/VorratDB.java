package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VorratDB implements Vorrat {

    //private static final String DB_URL = "jdbc:h2:file:C:/Users/rausch/Programmieren_GitRepo/Pro12/restaurant/data/vorratdb";
    private static final String DB_URL = "jdbc:h2:tcp://localhost:9092/C:/Users/rausch/Programmieren_GitRepo/Pro12/restaurant/data/vorratdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private Map<String, Integer> vorratMap = new HashMap<>();
    private Map<String, String> produktZuKategorie = new HashMap<>();
    private Map<String, Double> produktPreise = new HashMap<>();

    @Override
    public Map<String, Integer> getVorratMap() {
        return vorratMap;
    }

    @Override
    public Map<String, String> getProduktZuKategorie() {
        return produktZuKategorie;
    }

    @Override
    public Map<String, Double> getProduktPreise() {
        return produktPreise;
    }

    @Override
    public void ladeVorrat() {
        vorratMap.clear();
        produktZuKategorie.clear();
        produktPreise.clear();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, kategorie, anzahl, preis FROM produkt")) {

            while (rs.next()) {
                String name = rs.getString("name");
                String kategorie = rs.getString("kategorie");
                int anzahl = rs.getInt("anzahl");
                double preis = rs.getDouble("preis");

                vorratMap.put(name, anzahl);
                produktZuKategorie.put(name, kategorie);
                produktPreise.put(name, preis);
            }

        } catch (SQLException e) {
            System.out.println("*** Fehler beim Lesen der Datenbank: " + e.getMessage());
        }
    }

    @Override
    public void schreibeVorrat() {
        String backupTable = "produkt_backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Backup erstellen
            stmt.executeUpdate("CREATE TABLE " + backupTable + " AS SELECT * FROM produkt");

            // Bestehende Tabelle löschen
            stmt.executeUpdate("DELETE FROM produkt");

            // Neue Daten einfügen
            String insertSQL = "INSERT INTO produkt (name, kategorie, anzahl, preis) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                List<String> kategorienReihenfolge = List.of("Basisgericht", "Beilage", "Getraenk");

                for (String kategorie : kategorienReihenfolge) {
                    vorratMap.keySet().stream()
                            .filter(name -> kategorie.equals(produktZuKategorie.get(name)))
                            .sorted()
                            .forEach(name -> {
                                try {
                                    pstmt.setString(1, name);
                                    pstmt.setString(2, kategorie);
                                    pstmt.setInt(3, vorratMap.getOrDefault(name, 0));
                                    pstmt.setDouble(4, produktPreise.getOrDefault(name, 0.0));
                                    pstmt.executeUpdate();
                                } catch (SQLException e) {
                                    System.out.println("*** Fehler beim Einfügen von " + name + ": " + e.getMessage());
                                }
                            });
                }
            }

        } catch (SQLException e) {
            System.out.println("*** Fehler beim Schreiben in die Datenbank: " + e.getMessage());
        }
    }
}

