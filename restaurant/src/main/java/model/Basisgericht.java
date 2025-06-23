package model;

public abstract class Basisgericht implements Produkt {
    protected String name;

    public Basisgericht(String name) {
        this.name = name;
    }

    @Override
    public String getName() {   
        return name;
    }
}