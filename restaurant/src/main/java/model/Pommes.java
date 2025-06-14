package model;

public class Pommes extends Beilage { 
    public Pommes(Gericht gericht) { 
        super(gericht); 
    } 
    @Override
    public void druckeBeschreibung() { 
        gericht.druckeBeschreibung(); 
        System.out.print(", Pommes"); 
    } 
    @Override
    public double getPreis() { 
        return gericht.getPreis() + 2.50; 
    } 
} 
