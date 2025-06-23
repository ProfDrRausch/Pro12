package controller;
import model.Warenkorb;

public class Main {
    public static void main(String[] args) {
        Warenkorb warenkorb = new Warenkorb();
        warenkorb.ladeVorrat("vorrat.txt");

        MenuHandler menu = new MenuHandler(warenkorb);
        menu.start();
    }
}
