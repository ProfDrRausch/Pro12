package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class VorratXML implements Vorrat {
    private static final String XML_DATEI = "restaurant/data/vorrat.xml";
    // private static final String DTD_DATEI = "restaurant/data/vorrat.dtd";

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
        File xmlFile = new File(XML_DATEI);
        if (!xmlFile.exists()) {
            System.out.println("*** XML-Datei " + xmlFile.getPath() + " nicht gefunden!");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(false); // DTD benötigt kein Namespace

            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new org.xml.sax.helpers.DefaultHandler() {
                @Override
                public void warning(SAXParseException e) {
                    System.out.printf("⚠️  Warnung in Zeile %d: %s%n", e.getLineNumber(), e.getMessage());
                }

                @Override
                public void error(SAXParseException e) throws SAXException {
                    System.err.printf("*** Fehler in Zeile %d, Spalte %d:%n→ %s%n",
                            e.getLineNumber(), e.getColumnNumber(), e.getMessage());
                    throw e;
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    System.err.printf("*** FATAL in Zeile %d, Spalte %d:%n→ %s%n",
                            e.getLineNumber(), e.getColumnNumber(), e.getMessage());
                    throw e;
                }
            });

            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("✔️  XML ist gültig laut DTD.");

            NodeList produkte = doc.getElementsByTagName("produkt");

            for (int i = 0; i < produkte.getLength(); i++) {
                Element produkt = (Element) produkte.item(i);

                String kategorie = produkt.getAttribute("kategorie").trim();
                String name = getText(produkt, "name");
                int menge = Integer.parseInt(getText(produkt, "anzahl"));
                double preis = Double.parseDouble(getText(produkt, "preis"));

                vorratMap.put(name, menge);
                produktZuKategorie.put(name, kategorie);
                produktPreise.put(name, preis);
            }

        } catch (SAXException e) {
            System.err.println("*** Die XML-Datei ist nicht gültig laut DTD:");
            System.err.println("→ " + e.getMessage());
        } catch (Exception e) {
            System.err.println("*** Fehler beim Verarbeiten der XML-Datei:");
            System.err.println("→ " + e.getMessage());
        }
    }

    private String getText(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent().trim();
        }
        return "";
    }

    @Override
    public void schreibeVorrat() {
        File dataOrdner = new File("restaurant/data");
        if (!dataOrdner.exists()) {
            dataOrdner.mkdirs();
        }

        File original = new File(dataOrdner, "vorrat.xml");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH-mm-ss"));
        File backup = new File(dataOrdner, timestamp + "_vorrat.xml");

        try {
            if (original.exists()) {
                Files.copy(original.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                System.out.println("*** Es liegt noch keine vorrat.xml vor – sie wird neu erstellt.");
            }
        } catch (IOException e) {
            System.out.println("*** Fehler beim Erstellen des Backups: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(original))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.newLine();
            writer.write("<!DOCTYPE vorrat SYSTEM \"vorrat.dtd\">");
            writer.newLine();
            writer.write("<vorrat>");
            writer.newLine();

            List<String> kategorienReihenfolge = List.of("Basisgericht", "Beilage", "Getraenk");

            for (String kategorie : kategorienReihenfolge) {
                vorratMap.keySet().stream()
                        .filter(name -> kategorie.equals(produktZuKategorie.get(name)))
                        .sorted()
                        .forEach(name -> {
                            int menge = vorratMap.getOrDefault(name, 0);
                            double preis = produktPreise.getOrDefault(name, 0.0);
                            try {
                                writer.write("    <produkt kategorie=\"" + kategorie + "\">");
                                writer.newLine();
                                writer.write("        <name>" + name + "</name>");
                                writer.newLine();
                                writer.write("        <anzahl>" + menge + "</anzahl>");
                                writer.newLine();
                                writer.write(String.format(Locale.US, "        <preis>%.2f</preis>", preis));

                                writer.newLine();
                                writer.write("    </produkt>");
                                writer.newLine();
                            } catch (IOException e) {
                                System.out.println("*** Fehler beim Schreiben von " + name + ": " + e.getMessage());
                            }
                        });
            }

            writer.write("</vorrat>");
            writer.newLine();

            System.out.println("✔️  Vorrat wurde als XML-Datei gespeichert.");

        } catch (IOException e) {
            System.out.println("*** Fehler beim Schreiben der XML-Datei: " + e.getMessage());
        }
    }
}
