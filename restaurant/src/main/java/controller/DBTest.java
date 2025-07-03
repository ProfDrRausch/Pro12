package controller;

import java.sql.*;

public class DBTest {

    //private static final String DB_URL = "jdbc:h2:file:C:/Users/rausch/Programmieren_GitRepo/Pro12/restaurant/data/vorratdb";
    private static final String DB_URL = "jdbc:h2:tcp://localhost:9092/C:/Users/rausch/Programmieren_GitRepo/Pro12/restaurant/data/vorratdb";

    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, kategorie, anzahl, preis FROM produkt ORDER BY kategorie, name")) {

            System.out.printf("%-20s %-15s %6s %8s%n", "Name", "Kategorie", "Menge", "Preis");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                String name = rs.getString("name");
                String kategorie = rs.getString("kategorie");
                int anzahl = rs.getInt("anzahl");
                double preis = rs.getDouble("preis");

                System.out.printf("%-20s %-15s %6d %8.2f%n", name, kategorie, anzahl, preis);
            }

        } catch (SQLException e) {
            System.out.println("*** Fehler beim Zugriff auf die Datenbank: " + e.getMessage());
        }
    }
}

