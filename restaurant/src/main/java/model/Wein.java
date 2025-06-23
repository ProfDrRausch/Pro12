package model;

public class Wein extends Getraenk {
    public Wein() {
        super("Wein");
    }

    @Override
    public double getPreis() {
        return 4.0;
    }
}