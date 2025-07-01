package model;

public abstract class Beilage implements Produkt {
    private String name;
    private double preis;

    public Beilage(String name, double preis) {
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