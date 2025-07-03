package controller;

import model.Vorrat;
import model.VorratDB;
import model.Warenkorb;

public class Main {
    public static void main(String[] args) {
        //Vorrat vorrat = new VorratDatei();
        //Vorrat vorrat = new VorratXML();
        Vorrat vorrat = new VorratDB();
        vorrat.ladeVorrat();
        Warenkorb warenkorb = new Warenkorb(vorrat);;
        MenuHandler menu = new MenuHandler(vorrat,warenkorb);
        menu.start();
        vorrat.schreibeVorrat();
        System.out.println("\nIhre endgültige Bestellung:");
        warenkorb.druckeWarenkorb();
        System.out.println("Vielen Dank - guten Appetit! 🍽️");
    }
}
