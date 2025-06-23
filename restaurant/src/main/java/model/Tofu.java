package model;

public class Tofu extends Basisgericht {
    public Tofu() {
        super("Tofu");
    }

    @Override
    public double getPreis() {
        return 8.5;
    }
}