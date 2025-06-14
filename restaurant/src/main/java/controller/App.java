package controller;

import java.util.logging.Logger;

import model.Gericht;
import model.Hueftsteak;
import model.Nudeln;
import model.Salat;
import model.Suppe;

// https://www.philipphauer.de/study/se/design-pattern/decorator.php

public class App {
    public static void main(String[] args) throws Exception {

        Logger logger = Logger.getLogger(App.class.getName());
        logger.info("This is a module-using App!");

        Gericht gericht = new Salat(new Nudeln(new Hueftsteak()));
        gericht.druckeBeschreibung();
        // Hüftsteak, Nudeln, Salat
        System.out.println(" für " + gericht.getPreis() + " Euro");
        // für 19.75 Euro

        gericht = new Suppe(gericht);
        gericht.druckeBeschreibung();
        // Hüftsteak, Nudeln, Salat, Suppe
        System.out.println(" für " + gericht.getPreis() + " Euro");
        // für 21.25 Euro
    }
}
