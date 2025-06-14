package model;

public class Salat extends Beilage { 
    public Salat(Gericht gericht) { 
        super(gericht); 
    } 
    @Override
    public void druckeBeschreibung() { 
        gericht.druckeBeschreibung(); 
        System.out.print(", Salat"); 
    } 
    @Override
    public double getPreis() { 
        return gericht.getPreis() + 2.25; 
    } 
} 

