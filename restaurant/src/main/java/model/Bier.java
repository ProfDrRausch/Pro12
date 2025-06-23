package model;

public class Bier extends Getraenk {
    public Bier() {
        super("Bier");
    }

    @Override
    public double getPreis() {
        return 3.0;
    }
}