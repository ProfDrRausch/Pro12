package model;

public class Wienerschnitzel extends Basisgericht {
    public Wienerschnitzel() {
        super("Wienerschnitzel");
    }

    @Override
    public double getPreis() {
        return 10.5;
    }
}