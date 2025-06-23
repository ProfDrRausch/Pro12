package model;

public class Salat extends Beilage {
    public Salat() {
        super("Salat");
    }

    @Override
    public double getPreis() {
        return 2.25;
    }

}