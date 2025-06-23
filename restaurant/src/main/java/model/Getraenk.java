package model;

public abstract class Getraenk implements Produkt {
    protected String name;

    public Getraenk(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}