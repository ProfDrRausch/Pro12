package model;

public abstract class Basisgericht implements Produkt {
    private String name;
    private double preis;

    public Basisgericht(String name, double preis) {
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