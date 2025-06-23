package model;

public class Bratkartoffeln extends Beilage {
    public Bratkartoffeln() {
        super("Bratkartoffeln");
    }

    @Override
    public double getPreis() {
        return 1.5;
    }
}