package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import controller.MenuHandler;
import model.Produkt;
import model.Vorrat;
import model.Warenkorb;

public class RestaurantGUI extends JFrame {
    private final MenuHandler menuHandler;
    private final Warenkorb warenkorb;
    private final Vorrat vorrat;
    
    private JTextArea warenkorbTextArea;

    public RestaurantGUI(MenuHandler menuHandler, Warenkorb warenkorb, Vorrat vorrat) {
        this.menuHandler = menuHandler;
        this.warenkorb = warenkorb;
        this.vorrat = vorrat;

        setTitle("HFT-Restaurant");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        updateWarenkorbAnzeige();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel headerLabel = new JLabel("HFT-Restaurant", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);

        warenkorbTextArea = new JTextArea(15, 30);
        warenkorbTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        warenkorbTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(warenkorbTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 1, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton basisgerichtButton = new JButton("Basisgericht wählen");
        basisgerichtButton.addActionListener(e -> waehleBasisgericht());
        controlPanel.add(basisgerichtButton);

        JButton beilageHinzufuegenButton = new JButton("Beilage hinzufügen");
        beilageHinzufuegenButton.addActionListener(e -> beilageHinzufuegen());
        controlPanel.add(beilageHinzufuegenButton);

        JButton beilageEntfernenButton = new JButton("Beilage entfernen");
        beilageEntfernenButton.addActionListener(e -> beilageEntfernen());
        controlPanel.add(beilageEntfernenButton);
        
        JButton getraenkButton = new JButton("Getränk wählen");
        getraenkButton.addActionListener(e -> waehleGetraenk());
        controlPanel.add(getraenkButton);

        JButton vorratAnzeigenButton = new JButton("Vorrat anzeigen");
        vorratAnzeigenButton.addActionListener(e -> zeigeVorrat());
        controlPanel.add(vorratAnzeigenButton);

        JButton bestellungAbschliessenButton = new JButton("Bestellung abschließen");
        bestellungAbschliessenButton.addActionListener(e -> bestellungAbschliessen());
        controlPanel.add(bestellungAbschliessenButton);

        add(controlPanel, BorderLayout.EAST);
    }

    private void updateWarenkorbAnzeige() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ihr aktueller Warenkorb:\n");
        sb.append("========================\n\n");
        
        sb.append("Basisgericht:\n");
        Produkt basisgericht = warenkorb.getBasisgericht();
        if (basisgericht != null) {
            sb.append(String.format("  %-20s %6.2f €%n", basisgericht.getName(), basisgericht.getPreis()));
        } else {
            sb.append("  (noch keins)\n");
        }

        sb.append("\nBeilagen:\n");
        List<Produkt> beilagen = warenkorb.getBeilagen();
        if (beilagen.isEmpty()) {
             sb.append("  (keine)\n");
        } else {
            for (Produkt beilage : beilagen) {
                sb.append(String.format("  %-20s %6.2f €%n", beilage.getName(), beilage.getPreis()));
            }
        }

        sb.append("\nGetränk:\n");
        Produkt getraenk = warenkorb.getGetraenk();
        if (getraenk != null) {
            sb.append(String.format("  %-20s %6.2f €%n", getraenk.getName(), getraenk.getPreis()));
        } else {
            sb.append("  (noch keins)\n");
        }

        sb.append("\n========================\n");
        sb.append(String.format("Gesamtpreis: %18.2f €%n", warenkorb.getGesamtpreis()));

        warenkorbTextArea.setText(sb.toString());
    }

    private void waehleBasisgericht() {
        String[] options = {"1: Garnelen", "2: Hueftsteak", "3: Tofu", "4: Wienerschnitzel"};
        String choice = (String) JOptionPane.showInputDialog(this, "Wählen Sie ein Basisgericht:", "Basisgericht wählen", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String eingabe = choice.split(":")[0].trim();
            if (menuHandler.waehleBasisgericht(eingabe)) {
                updateWarenkorbAnzeige();
            } else {
                JOptionPane.showMessageDialog(this, "Ungültige Auswahl.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void beilageHinzufuegen() {
        String[] options = {"1: Pommes", "2: Bratkartoffeln", "3: Nudeln", "4: Salat", "5: Suppe"};
        String choice = (String) JOptionPane.showInputDialog(this, "Wählen Sie eine Beilage:", "Beilage hinzufügen", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String eingabe = choice.split(":")[0].trim();
            if (menuHandler.beilageHinzufuegen(eingabe)) {
                updateWarenkorbAnzeige();
            } else {
                JOptionPane.showMessageDialog(this, "Ungültige Auswahl.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void beilageEntfernen() {
        List<Produkt> beilagen = warenkorb.getBeilagen();
        if (beilagen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Es sind keine Beilagen vorhanden.", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] beilageNamen = new String[beilagen.size()];
        for (int i = 0; i < beilagen.size(); i++) {
            beilageNamen[i] = (i + 1) + ": " + beilagen.get(i).getName();
        }
        
        String choice = (String) JOptionPane.showInputDialog(this, "Welche Beilage möchten Sie entfernen?", "Beilage entfernen", JOptionPane.QUESTION_MESSAGE, null, beilageNamen, beilageNamen[0]);

        if (choice != null) {
            int index = Integer.parseInt(choice.split(":")[0].trim()) - 1;
            if (menuHandler.beilageEntfernen(index)) {
                updateWarenkorbAnzeige();
            } else {
                 JOptionPane.showMessageDialog(this, "Fehler beim Entfernen der Beilage.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void waehleGetraenk() {
        String[] options = {"1: Wasser", "2: Bier", "3: Wein"};
        String choice = (String) JOptionPane.showInputDialog(this, "Wählen Sie ein Getränk:", "Getränk wählen", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String eingabe = choice.split(":")[0].trim();
            if (menuHandler.getraenkWaehlen(eingabe)) {
                updateWarenkorbAnzeige();
            } else {
                JOptionPane.showMessageDialog(this, "Ungültige Auswahl.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void zeigeVorrat() {
        String vorratString = vorrat.getVorratAsString();
        
        JTextArea vorratTextArea = new JTextArea(vorratString);
        vorratTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        vorratTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(vorratTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Aktueller Vorrat", JOptionPane.INFORMATION_MESSAGE);
    }

    private void bestellungAbschliessen() {
        menuHandler.bestellungAbschliessen();
        JOptionPane.showMessageDialog(this, "Vielen Dank - guten Appetit! 🍽️", "Bestellung abgeschlossen", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}