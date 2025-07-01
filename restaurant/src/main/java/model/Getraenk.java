package model;

public abstract class Getraenk implements Produkt {
    private String name;
    private double preis;

    public Getraenk(String name, double preis) {
        this.name = name;
        this.preis = preis; 
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public double getPreis() {      
        return preis;
    }
}