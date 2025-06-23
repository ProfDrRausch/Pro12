package model;

public class Garnelen extends Basisgericht {
    public Garnelen() {
        super("Garnelen");
    }

    @Override
    public double getPreis() {
        return 13.5;
    }
}