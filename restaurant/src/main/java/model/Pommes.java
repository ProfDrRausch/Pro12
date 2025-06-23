package model;

public class Pommes extends Beilage {
    public Pommes() {
        super("Pommes");
    }

    @Override
    public double getPreis() {
        return 2.5;
    }
}