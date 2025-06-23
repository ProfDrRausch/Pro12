package model;

public class Wasser extends Getraenk {
    public Wasser() {
        super("Wasser");
    }

    @Override
    public double getPreis() {
        return 1.5;
    }
}